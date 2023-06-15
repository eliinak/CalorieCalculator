import axios from 'axios'
const API_URL = 'http://localhost:8080/app'

class ProfileService {
  getAllUsers () {
    return axios.get(API_URL + '/user')
  }

  getUserByiId (id) {
    return axios.get(API_URL + '/search/id',
      {
        params:
          {
            id: id
          }
      })
  }

  saveData (filters) {
    const format = new FormData()
    format.append('id', filters.id)
    format.append('name', filters.name)
    format.append('surname', filters.surname)
    format.append('age', filters.age)
    format.append('email', filters.email)
    format.append('gender', filters.gender)
    format.append('height', filters.height)
    format.append('kilograms', filters.kilograms)
    format.append('lifestyle', filters.lifestyle)

    return axios.post(API_URL + '/save', format)
  }

  checkEmailExists (email) {
    return axios.get(`${API_URL}/checkEmailExists`, {
      params: {
        email: email
      }
    })
  }

  saveKcal (userr) {
    const format = new FormData()
    format.append('needKcal', userr.needKcal)

    return axios.post(API_URL + '/saveKcal', format)
  }
}
export default new ProfileService()
