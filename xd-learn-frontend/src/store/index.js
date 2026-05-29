import { createStore } from 'vuex'

// 创建store实例
const store = createStore({
  state() {
    return {
      count: 0
    }
  },
  mutations: {
    increment(state) {
      state.count++
    }
  },
  actions: {
    increment(context) {
      context.commit('increment')
    }
  },
  getters: {
    doubleCount(state) {
      return state.count * 2
    }
  }
})

export default store