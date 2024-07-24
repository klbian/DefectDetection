// 页面白名单，不受拦截
const whiteList = [
	'/pages/index/index',
	'/pages/detect/detect',
	'/pages/mine/mine',
	'/pages/login/login',
	'/',
]
function hasPermission (url) {
    //isLogin是登录成功后在本地存储登录标识，存储一个能够判断用户登录的唯一标识就行，根据自己存储的数据类型来判断
	let islogin = uni.getStorageSync('isLogin');
	
    // 在白名单中或有登录判断条件可以直接跳转
    if(whiteList.indexOf(url) !== -1 || islogin==1) {
		console.log('跳转的页面在白名单内或是已登录')
        return true
    }
	console.log('跳转的页面不在白名单内且未登录')
    return false
}
uni.addInterceptor('navigateTo', {
    // 页面跳转前进行拦截, invoke根据返回值进行判断是否继续执行跳转
    invoke (e) {
        if(!hasPermission(e.url)){
            uni.navigateTo({
                url: '/pages/login/login'
            })
            return false
        }
        return true
    },
    success (e) {
		
    }
})
 
uni.addInterceptor('switchTab', {
    // tabbar页面跳转前进行拦截
    invoke (e) {
		
        if(!hasPermission(e.url)){
			
            uni.navigateTo({
                url: '/pages/login/login'
            })
			
            return false
        }
		
        return true
    },
    success (e) {
		
    }
})