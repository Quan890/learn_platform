// 导入HTTP请求工具
import request from '@/utils/request'

/**
 * 向AI助手提问接口
 * @param {String} question - 用户问题
 * @returns {Promise} AI助手回答Promise
 */
export function askAI(question) {
  return request({
    url: '/ai/ask',
    method: 'post',
    data: { question }
  })
}