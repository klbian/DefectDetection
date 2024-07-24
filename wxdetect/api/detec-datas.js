export default[{
	chartData:{
	  categories: ["40", "80", "120", "160", "200", "240", "280", "320"],
	  series: [{
	    name: "目标值",
	    data: [0.81, 0.819, 0.815, 0.824,0.833, 0.819, 0.84, 0.851]
	  }]
	},
	pieData: {
	    "series":[
	        {"name":"划痕Sc","data":57},
	        {"name":"夹杂In","data":30},
	        {"name":"点蚀Ps","data":20},
	        {"name":"开裂Cr","data":18},
	        {"name":"斑块Ra","data":8},
			{"name":"氧化铁皮压入RS","data":8},
	    ],
	},
	alarmData:{
		categories: ["1", "2", "3"],
		series: [{
		  name: "警告数",
		  data: [2, 1, 0]
		}]
	}
}]