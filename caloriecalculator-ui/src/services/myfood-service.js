import axios from 'axios'

const API_URL = 'http://localhost:8080/app'

class MyfoodService {
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

  addFood (userFood) {
    const format = new FormData()
    format.append('user_id', userFood.user)
    format.append('food_id', userFood.food)
    format.append('food_kcal', userFood.kcal)
    format.append('food_protein', userFood.protein)
    format.append('food_grams', userFood.grams)
    format.append('food_fat', userFood.fat)
    format.append('food_carbs', userFood.carbs)

    return axios.post(API_URL + '/saveFood', format)
  }
}

export default new MyfoodService()
