<template>
  <div class="back">
    <div class="profileform">
      <b-card class="form">
        <b-form-group
          label-cols-lg="8"
          label="Моето дневно меню"
          label-size="lg"
          label-align-sm="right"
          label-class="font-weight-bold pt-0"
          class="title"
        ></b-form-group>
        <div class="filterFoods">
          <input v-model="filterText" placeholder="Въведете ястие" />
          <b-form-select v-model="selected" :options="filteredFoodOptions"></b-form-select>
          <div class="mt-3">Selected: <strong>{{ selected }}</strong></div>
        </div>
        <b-form-group label-for="grams" class="grams">
          <b-input-group prepend="грама" class="grams">
            <b-form-input type="number" id="grams" v-model.number="currentFood.grams"></b-form-input>
          </b-input-group>
          <b-form-group>
            <b-button-group>
              <b-button class="buttonAddFood" v-on:click="addFood">Добави ястие</b-button>
            </b-button-group>
          </b-form-group>
          <b-button-group>
            <b-button class="buttonSaveFood" v-on:click="saveMeal">Изчисли</b-button>
          </b-button-group>
        </b-form-group>
        <div>
          <button class="buttonAddFood" @click="showAddFoodForm = true">Добави ново ястие</button>
          <div v-if="showAddFoodForm">
            <h4>Добави ново ястие за 100гр.</h4>
            <form @submit.prevent="addNewFoodOption">
                <label for="foodName">Име:</label>
                <input id="foodName" type="text" v-model="newFood.food" required>
                <label for="foodKcal">Kcal:</label>
                <input id="foodKcal" type="number" v-model.number="newFood.kcal" required>
              <div class="form-group-food">
                <label for="foodProtein">Протеин:</label>
                <input id="foodProtein" type="number" v-model.number="newFood.protein" required>
              </div>
                <label for="foodFat">Мазнини:</label>
                <input id="foodFat" type="number" v-model.number="newFood.fat" required>
                <label for="foodCarbs">Въглехидрати:</label>
                <input id="foodCarbs" type="number" v-model.number="newFood.carbs" required>
              <button class="buttonAddFood" type="submit">Добави ястие</button>
            </form>
          </div>
        </div>
        <div v-if="mealFoods.length > 0" class="meal-summary">
          <h4>Консумирани храни:</h4>
          <table class="table table-bordered">
            <thead>
            <tr>
              <th>Храна</th>
              <th>Грама</th>
              <th>Kcal</th>
              <th>Протеин</th>
              <th>Мазнини</th>
              <th>Въглехидрати</th>
              <th>Действия</th>
            </tr>
            </thead>
            <tbody>
            <tr v-for="food in mealFoods" :key="food.id">
              <td>{{ food.food }}</td>
              <td>{{ food.grams }}</td>
              <td>{{ food.kcal }}</td>
              <td>{{ food.protein }}</td>
              <td>{{ food.fat }}</td>
              <td>{{ food.carbs }}</td>
              <td>
                <button class="buttonRemoveFood" @click="removeFood(food)">Премахни</button>
              </td>
            </tr>
            <tr>
              <th>Общо</th>
              <td></td>
              <td>{{ mealTotals.kcal }}</td>
              <td>{{ mealTotals.protein }}</td>
              <td>{{ mealTotals.fat }}</td>
              <td>{{ mealTotals.carbs }}</td>
              <td></td>
            </tr>
            </tbody>
          </table>
        </div>
      </b-card>
    </div>
    <div class="container">
    <div class="chart-container">
      <canvas id="mealChart"></canvas>
    </div>
    <div class="chart-container">
      <canvas id="kcalChart"></canvas>
    </div>
    </div>
  </div>
</template>

<script>
import Chart from 'chart.js/auto'
import axios from 'axios'
import router from '@/router'

export default {
  name: 'MyFood',
  computed: {
    currentUser () {
      return this.$store.state.auth.user
    },
    filteredFoodOptions () {
      if (!this.filterText) {
        return this.foodOptions
      }
      const filterLowerCase = this.filterText.toLowerCase()
      return this.foodOptions.filter((option) =>
        option.text.toLowerCase().includes(filterLowerCase)
      )
    }
  },
  data () {
    return {
      filterText: '',
      showAddFoodForm: false,
      newFood: {
        food: '',
        kcal: null,
        protein: null,
        fat: null,
        carbs: null
      },
      selected: null,
      foodOptions: [],
      currentFood: {
        food: '',
        grams: 0,
        kcal: 0,
        protein: 0,
        fat: 0,
        carbs: 0
      },
      mealFoods: [],
      mealTotals: {
        kcal: 0,
        protein: 0,
        fat: 0,
        carbs: 0
      },
      pieChart: null,
      kcalChart: null,
      needKcal: ''
    }
  },
  methods: {
    removeFood (food) {
      const index = this.mealFoods.indexOf(food)
      if (index !== -1) {
        this.mealFoods.splice(index, 1)
        this.updateMealTotals()
      }
    },
    fetchFoodOptions () {
      const API_URL = 'http://localhost:8080/app/food-options'
      axios
        .get(API_URL)
        .then(response => {
          this.foodOptions = response.data.map(food => ({
            text: food.food,
            value: food.food,
            kcal: food.kcal,
            protein: food.protein,
            fat: food.fat,
            carbs: food.carbs
          }))
        })
        .catch(error => {
          console.error('Error fetching food options:', error)
        })
    },
    addNewFoodOption () {
      const API_URL = 'http://localhost:8080/app/create-food-options'

      axios
        .post(API_URL, this.newFood)
        .then(() => {
          this.foodOptions.push({
            text: this.newFood.food,
            value: this.newFood.food,
            kcal: this.newFood.kcal,
            protein: this.newFood.protein,
            fat: this.newFood.fat,
            carbs: this.newFood.carbs
          })

          this.newFood.food = ''
          this.newFood.kcal = null
          this.newFood.protein = null
          this.newFood.fat = null
          this.newFood.carbs = null
          this.showAddFoodForm = false
        })
        .catch((error) => {
          console.error('Error adding new food option:', error)
        })
    },
    calculateFoodValues () {
      const selectedFood = this.selected
      const grams = this.currentFood.grams

      if (selectedFood && grams) {
        const foodOption = this.foodOptions.find(food => food.value === selectedFood)

        if (foodOption) {
          this.currentFood.kcal = (foodOption.kcal * grams) / 100
          this.currentFood.protein = (foodOption.protein * grams) / 100
          this.currentFood.fat = (foodOption.fat * grams) / 100
          this.currentFood.carbs = (foodOption.carbs * grams) / 100
        }
      } else {
        this.currentFood.kcal = 0
        this.currentFood.protein = 0
        this.currentFood.fat = 0
        this.currentFood.carbs = 0
      }
    },
    addFood () {
      this.calculateFoodValues()

      const newFood = {
        food: this.selected,
        grams: this.currentFood.grams,
        kcal: this.currentFood.kcal,
        protein: this.currentFood.protein,
        fat: this.currentFood.fat,
        carbs: this.currentFood.carbs
      }

      this.mealFoods.push(newFood)

      this.currentFood.food = ''
      this.currentFood.grams = 0
      this.currentFood.kcal = 0
      this.currentFood.protein = 0
      this.currentFood.fat = 0
      this.currentFood.carbs = 0

      this.updateMealTotals()
    },
    updateMealTotals () {
      this.mealTotals.kcal = this.mealFoods.reduce((total, food) => total + food.kcal, 0)
      this.mealTotals.protein = this.mealFoods.reduce((total, food) => total + food.protein, 0)
      this.mealTotals.fat = this.mealFoods.reduce((total, food) => total + food.fat, 0)
      this.mealTotals.carbs = this.mealFoods.reduce((total, food) => total + food.carbs, 0)
    },
    saveMeal () {
      this.updateMealTotals()
      this.displayChart()
      this.updateKcalChart()
      this.displayCharts()
    },
    displayChart () {
      this.destroyCharts()
      this.pieChart = this.createPieChart()
      this.updatePieChart()
      this.kcalChart = this.createKcalChart()
      this.updateKcalChart()
    },
    createPieChart () {
      const canvas = document.getElementById('mealChart')
      const consumedProtein = this.mealTotals.protein
      const consumedFat = this.mealTotals.fat
      const consumedCarbs = this.mealTotals.carbs

      const data = {
        labels: ['Протеин', 'Мазнини', 'Въглехидрати'],
        datasets: [
          {
            data: [consumedProtein, consumedFat, consumedCarbs],
            backgroundColor: ['#42b983', '#f4511e', '#039be5']
          }
        ]
      }

      const options = {
        responsive: true,
        maintainAspectRatio: false,
        plugins: {
          title: {
            display: true,
            text: `Макронутриенти ${status}`,
            color: '#000',
            font: {
              size: 18,
              color: '#42b983'
            },
            padding: {
              bottom: 10
            }
          },
          legend: {
            labels: {
              color: '#000'
            },
            padding: 1
          }
        }
      }

      return new Chart(canvas, {
        type: 'doughnut',
        data,
        options
      })
    },
    updatePieChart () {
      const consumedProtein = this.mealTotals.protein
      const consumedFat = this.mealTotals.fat
      const consumedCarbs = this.mealTotals.carbs

      if (this.pieChart) {
        this.pieChart.data.datasets[0].data = [
          consumedProtein,
          consumedFat,
          consumedCarbs
        ]
        this.pieChart.update()
      }
    },
    createKcalChart () {
      const canvas = document.getElementById('kcalChart')
      // const needKcal = this.currentUser.needKcal
      console.log(this.needKcal)
      const consumedKcal = this.mealTotals.kcal
      let status = ''

      if (consumedKcal < this.needKcal) {
        status = 'Калориен дефицит'
      } else if (consumedKcal > this.needKcal) {
        status = 'Калориен излишък'
      } else {
        status = 'Баланс'
      }

      const data = {
        labels: ['Консумирани Kcal', 'Оставащи Kcal'],
        datasets: [
          {
            data: [consumedKcal, this.needKcal - consumedKcal],
            backgroundColor: ['#42b983', '#f4511e'],
            color: '#42b983'
          }
        ]
      }
      const options = {
        responsive: true,
        maintainAspectRatio: false,
        plugins: {
          title: {
            color: '#000',
            display: true,
            text: `Kcal Статус: ${status}`,
            font: {
              size: 18
            },
            padding: {
              bottom: 10
            }
          },
          legend: {
            labels: {
              color: '#000'
            }
          }
        }
      }

      return new Chart(canvas, {
        type: 'doughnut',
        data,
        options
      })
    },
    updateKcalChart () {
      const consumedKcal = this.mealTotals.kcal
      console.log(this.needKcal)

      if (this.kcalChart) {
        this.kcalChart.data.datasets[0].data = [consumedKcal, this.needKcal - consumedKcal]
        this.kcalChart.options.plugins.title.text = `Kcal Статус: ${this.getStatus(consumedKcal, this.needKcal)}`
        this.kcalChart.update()
      }
    },
    getStatus (consumedKcal, needKcal) {
      if (consumedKcal < needKcal) {
        return 'Калориен деифицит'
      } else if (consumedKcal > needKcal) {
        return 'Калориен излишък'
      } else {
        return 'Баланс'
      }
    },
    destroyCharts () {
      if (this.pieChart) {
        this.pieChart.destroy()
        this.pieChart = null
      }

      if (this.kcalChart) {
        this.kcalChart.destroy()
        this.kcalChart = null
      }
    },
    displayCharts () {
      if (this.pieChart) {
        this.updatePieChart()
      } else {
        this.pieChart = this.createPieChart()
      }

      if (this.kcalChart) {
        this.updateKcalChart()
      } else {
        this.kcalChart = this.createKcalChart()
      }
    },
    fetchUser () {
      const API_URL = 'http://localhost:8080/app'
      axios.get(API_URL + '/user', {
        params: {
          id: this.currentUser.id,
          needKcal: this.currentUser.needKcal
        }
      })
        .then(response => {
          if (response && response.data) {
            this.needKcal = response.data.needKcal
            console.log(response)
          } else {
            alert('Моля, попълнете своите данни в изгледа за профил.')
            router.push('/profile')
          }
        })
        .catch(error => {
          alert('Something went wrong')
          console.error(error.response.data)
        })
    }
  },
  mounted () {
    this.fetchUser()
    this.displayCharts()
    this.fetchFoodOptions()
  }
}
</script>

<style scoped>
.profileform{
  background-image: url('../assets/healthy.jpg');
  background-position: center;
  background-size: cover;
  position: fixed;
  width: 100%;
  height: 100%;
  object-fit: contain;
  top: 0px;
  margin-top: 70px;
  left: 0px;
  z-index: 0;
  fetch_format: auto;
  overflow: auto;
}
.form {
  background-image: url('../assets/profileTable.jpg');
  background-position: center;
  background-size: cover;
  width: 700px;
  margin-right: 750px;
  margin-left: 10px;
  margin-bottom: 50px;
  margin-top: 20px;
  border-style: solid;
  border-radius: 25px;
  border-width: 2px;
  overflow: auto;
}
.title{
  color: #42b983;
  size: auto;
}
.buttonAddFood {
  background-color: #42b983;
  margin-top: 10px;
}
.buttonSaveFood {
  background-color: #42b983;
  margin-top: 10px;
}
.container {
  margin-top: 0px;
  height: calc(100vh - 70px);
  overflow-y: auto;
}
.chart-container {
  width: 60%;
  height: 500px;
  margin: 20px auto;
  position: relative;
  display: inline-block;
  float: right;
}

.chart-container canvas {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
}
.buttonAddFood{
  background-color: #42b983;
  border-radius: 5px;
}
.buttonRemoveFood {
  background-color: #42b983;
  border-radius: 5px;
}
</style>
