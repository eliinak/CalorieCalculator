import axios from 'axios'

const API_URL = 'http://localhost:8080/app'

class UserViewService {
  getAllUsers () {
    return axios.get(API_URL + '/user')
  }

  // getUserByiId (id) {
  //   return axios.get(API_URL + '/search/id',
  //     {
  //       params:
  //         {
  //           id: id
  //         }
  //     })
  // }
}

export default new UserViewService()
