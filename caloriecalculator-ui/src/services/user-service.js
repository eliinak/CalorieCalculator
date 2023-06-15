import axios from 'axios'

const API_URL = 'http://localhost:8080/app'

class UserService {
  // getAllUsers () {
  //   return axios.get(API_URL + '/all')
  // }

  getAllUsers () {
    return axios.get(API_URL + '/user')
  }
}
export default new UserService()
