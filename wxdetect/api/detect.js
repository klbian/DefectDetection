import {myRequest} from '../network/http.js'
 
export function detect (config) {  
	return myRequest({
		url:'metalDetect/detect',
		method:'GET',
		data:config
	})
}