package com.ggbond.defectdetection.software.face;

import com.ggbond.defectdetection.dto.DetectResDto;
import com.ggbond.defectdetection.pojo.Defection;
import com.ggbond.defectdetection.software.data.DataModule;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.StackPane;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.jfree.chart.*;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.CategoryItemRenderer;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.chart.renderer.xy.XYBarRenderer;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.xy.DefaultXYDataset;
import org.jfree.data.xy.XYSeries;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.List;

/**
 * 实时检测界面
 * <p>
 * Author: 19461
 * Date: 2024/2/6
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Component
@DependsOn({"GUIAttributes","DataModule"})
public class RealtimeInterface extends JPanel {

    //子面板宽和高
    private final int sonWidth= (int) (GUIAttributes.attributes.width*0.6);
    private final int sonHeight=(int)(GUIAttributes.attributes.height*0.4);

    //标题
    TitledBorder titledBorder=new TitledBorder("实时检测");

    //检测画面
    private DetectImagePanel detectImagePanel=new DetectImagePanel();

    //图表
    private ChartsPanel chartsPanel=new ChartsPanel();

    //背景颜色
    Color backgroundColor=new Color(242,242,242);

    @EqualsAndHashCode(callSuper = true)
    @Data
    public class DetectImagePanel extends JPanel{

        //顶栏信息
        private JLabel topInfoLabel=new JLabel();

        //画面与表格面板
        private JPanel imageAndTablePanel=new JPanel();
        private JFXPanel imagePanel=new JFXPanel();
        private JScrollPane tablePanel =new JScrollPane();
        private JTable table=new JTable();

        public DetectImagePanel(){
            super();

            //设置布局
            this.setLayout(new BorderLayout());

            //设置顶部信息栏
            topInfoLabel.setText("当前工单: i号    完成度:n/m");
            topInfoLabel.setFont(new Font("黑体",Font.PLAIN,15));
            topInfoLabel.setBorder(new EmptyBorder(10,10,0,0));

            //画面与表格栏配置
            imageAndTablePanel.setLayout(new BoxLayout(imageAndTablePanel,BoxLayout.X_AXIS));
            imageAndTablePanel.setBorder(new EmptyBorder(5,5,10,5));

            //画面
            int screenWidth=(sonWidth/3)*2;
            int screenHeight=(sonHeight/5)*2;

            imagePanel.setPreferredSize(new Dimension(screenWidth-40,screenHeight-5));
            imagePanel.setBackground(Color.GRAY);

            Platform.runLater(this::initImageScreen);

            //表格
            tablePanel.setPreferredSize(new Dimension(sonWidth-screenWidth-20,screenHeight-50));

            tablePanel.add(table);
            tablePanel.setViewportView(table);

            imageAndTablePanel.add(imagePanel);
            imageAndTablePanel.add(tablePanel);

            table.setPreferredScrollableViewportSize(new Dimension(sonWidth-screenWidth-20,screenHeight-50));

            DefaultTableModel tableModel= (DefaultTableModel) table.getModel();
            String[] columns={"序号","缺陷","概率"};
            tableModel.addColumn(columns[0]);
            tableModel.addColumn(columns[1]);
            tableModel.addColumn(columns[2]);

            //添加组件
            this.add(BorderLayout.NORTH,topInfoLabel);
            this.add(Box.createHorizontalStrut(10));
            this.add(BorderLayout.CENTER,imageAndTablePanel);

            //配置
            this.setPreferredSize(new Dimension(sonWidth,sonHeight));
        }

        public void initImageScreen(){
            StackPane root=new StackPane();
            root.setStyle("-fx-background-color: #f2f2f2;");

            Image exampleImage=new Image("D:\\java.java\\defectDetection\\src\\main\\resources\\assets\\init.png",450,450,true,true);

            ImageView imageView=new ImageView(exampleImage);
            root.getChildren().add(imageView);

            Scene scene=new Scene(root);
            imagePanel.setScene(scene);
        }
        //更新标签
        public void updateTextLabel(int idx,int n,int m){
            topInfoLabel.setText(String.format("当前工单: %d号    完成度:%d/%d",idx,n,m));
            topInfoLabel.updateUI();
        }

        //更新画面
        public void updateImageAndTable(DetectResDto detectResDto){

            //更新画面
            byte[] imgBytes = Base64.getDecoder().decode(detectResDto.getImgBase64());
            Image img = new Image(new ByteArrayInputStream(imgBytes));
            ImageView imageView = new ImageView(img);
            imageView.setFitHeight(400.);
            imageView.setFitWidth(400.);
            StackPane root = new StackPane();
            root.setStyle("-fx-background-color:rgb(242, 242, 242) ;");
            root.setPrefSize(img.getWidth(), img.getHeight());
            root.getChildren().add(imageView);
            Scene scene = new Scene(root);
            imagePanel.setScene(scene);
            //更新表格
            this.resetTableData(detectResDto.getDefections());
        }


        public void resetTableData(List<Defection> defectionList){
            //清空原表格数据
            DefaultTableModel model=(DefaultTableModel) table.getModel();
            model.setRowCount(0);
            //添加新数据
            for(int i=0;i<defectionList.size();i++){
                Defection defection=defectionList.get(i);
                String[] rowData={i+"",defection.getCategory(),defection.getScore()+""};
                model.addRow(rowData);
            }
        }


    }

    @EqualsAndHashCode(callSuper = true)
    @Data
    public class ChartsPanel extends JPanel{

        private final static String chartsSavePath=GUIAttributes.attributes.getChartsSavePath();

        //图1
        JPanel lineAndBarPanel =new JPanel();
        ChartPanel lineAndBarChartPanel=new ChartPanel(null);

        //图2
        JPanel piePanel=new JPanel();
        ChartPanel pieChartPanel=new ChartPanel(null);

        public ChartsPanel(){

            //属性配置
            this.setPreferredSize(new Dimension(sonWidth,sonHeight));
            this.setBackground(backgroundColor);
            this.setBorder(new LineBorder(Color.GRAY,1));

            //表属性配置
            lineAndBarPanel.setBackground(backgroundColor);

            piePanel.setBackground(backgroundColor);

            int chart1Width=GUIAttributes.attributes.width/5;
            int chart1Height=GUIAttributes.attributes.height/3;

            int chart2Width=GUIAttributes.attributes.width/5;
            int chart2Height=GUIAttributes.attributes.height/3;

            lineAndBarChartPanel.setPreferredSize(new Dimension(chart1Width,chart1Height));
            pieChartPanel.setPreferredSize(new Dimension(chart2Width,chart2Height ));

            pieChartPanel.setBorder(new EmptyBorder(10,5,0,5));


            //设置布局
            BoxLayout boxLayout=new BoxLayout(this,BoxLayout.X_AXIS);
            this.setLayout(boxLayout);


            //更新图表
            this.updateCharts(null,null,null);

            //添加图表panel
            lineAndBarPanel.add(lineAndBarChartPanel);
            piePanel.add(pieChartPanel);
            this.add(lineAndBarPanel);
            this.add(piePanel);
        }

        //图表更新
        public void updateCharts(LinkedHashMap<Integer, Double> lineData, LinkedHashMap<Integer, Double> barData, LinkedHashMap<String,Integer> pieData){
            DefaultCategoryDataset lineDataset=new DefaultCategoryDataset();
            DefaultCategoryDataset barDataset=new DefaultCategoryDataset();
            DefaultPieDataset<String> pieDataset= new DefaultPieDataset<>();
            //如果数据源为空
            if(lineData==null){
                lineData=new LinkedHashMap<>();
                lineData.put(100,0.);
                lineData.put(200,0.);
                lineData.put(300,0.);
                lineData.put(400,0.);
                lineData.put(500,0.);
            }
            if(barData==null){
                barData=new LinkedHashMap<>();
                barData.put(100,0.);
                barData.put(200,0.);
                barData.put(300,0.);
                barData.put(400,0.);
                barData.put(500,0.);
            }
            if(pieData==null){
                pieData=new LinkedHashMap<>();
                pieData.put("",30);
                pieData.put("",30);
                pieData.put("",30);
                pieData.put("",10);
            }
            //重新设置数据
            lineData.forEach((key,value)->{
                lineDataset.addValue(value,"总缺陷率",key);
            });

            barData.forEach((key,value)->{
                barDataset.addValue(value,"单粒度缺陷率",key);
            });

            pieData.forEach(pieDataset::setValue);

            //渲染图像
            CategoryPlot plot=new CategoryPlot();

            CategoryItemRenderer lineRenderer=new LineAndShapeRenderer();
            plot.setDataset(0,lineDataset);
            plot.setRenderer(0,lineRenderer);


            CategoryItemRenderer barRenderer=new LineAndShapeRenderer();
            plot.setDataset(1,barDataset);
            plot.setRenderer(1,barRenderer);

            //配置plot
            plot.setDomainAxis(new CategoryAxis("检测数"));
            plot.setRangeAxis(new NumberAxis("缺陷率"));
            plot.setBackgroundPaint(backgroundColor);

            JFreeChart lineAndBarChart=new JFreeChart(plot);

            lineAndBarChart.getLegend().setVisible(true);
            lineAndBarChart.setTitle("缺陷率变化折线柱状图");

            lineAndBarChartPanel.setChart(lineAndBarChart);
            lineAndBarChartPanel.updateUI();

            //渲染图2
            ChartFactory.setChartTheme(createChartTheme("宋体"));
            JFreeChart pieChart= ChartFactory.createPieChart("缺陷占比饼图",pieDataset,false,true,true);
            pieChart.setBorderPaint(null);

            pieChartPanel.setChart(pieChart);
            pieChartPanel.updateUI();


            //保存图表
            try {
                ChartUtils.saveChartAsJPEG(new File(chartsSavePath+"缺陷率.jpeg"),lineAndBarChart,400,400);
                ChartUtils.saveChartAsJPEG(new File(chartsSavePath+"缺陷比例.jpeg"),pieChart,400,400);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public static StandardChartTheme createChartTheme(String fontName) {
            StandardChartTheme theme = new StandardChartTheme("unicode") {
                public void apply(JFreeChart chart) {
                    chart.getRenderingHints().put(RenderingHints.KEY_TEXT_ANTIALIASING,
                            RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);
                    super.apply(chart);
                }
            };
            fontName = StringUtils.isEmpty(fontName) ? "宋体" : fontName;
            theme.setExtraLargeFont(new Font(fontName, Font.PLAIN, 20));
            theme.setLargeFont(new Font(fontName, Font.PLAIN, 14));
            theme.setRegularFont(new Font(fontName, Font.PLAIN, 12));
            theme.setSmallFont(new Font(fontName, Font.PLAIN, 10));
            return theme;
        }

    }


    public RealtimeInterface(){
        super();

        //设置标题
        titledBorder.setTitleFont(new Font("黑体",Font.PLAIN,25));
        this.setBorder(titledBorder);

        //设置布局
        BoxLayout boxLayout=new BoxLayout(this,BoxLayout.Y_AXIS);
        this.setLayout(boxLayout);

        //添加子元素
        this.add(detectImagePanel);
        this.add(chartsPanel);

        //属性配置
        this.setBackground(backgroundColor);
        this.setVisible(true);
    }

    public void init(Map<String,LinkedHashMap> dataMaps,int idx,int n,int m){
        detectImagePanel.updateTextLabel(idx,n,m);
        updateCharts(dataMaps);
    }

    public void updateCharts(Map<String,LinkedHashMap> dataMaps){
        chartsPanel.updateCharts((LinkedHashMap<Integer, Double>) dataMaps.get("lineData"),
                (LinkedHashMap<Integer, Double>)dataMaps.get("barData"),
                (LinkedHashMap<String, Integer>) dataMaps.get("pieData"));
    }
}