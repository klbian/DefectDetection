let baseURL = '';
// 是否在控制台显示接口请求日志,本地环境启用,打包环境禁用
let showHttpLog = false;
// 测试环境
baseURL = 'http://localhost:8081/';
// 正式环境
// baseURL = 'XXXXX.XXXXX.com';
module.exports = {
	baseURL: baseURL,
	showHttpLog: showHttpLog
}