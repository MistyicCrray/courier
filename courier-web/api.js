import ajax from './ajax'
import {baseUrl} from './config'

export const getRquest = (url, params) => ajax.get(url, params)