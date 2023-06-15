<template>
  <div class="containerr" style="display: inline-flex">
    <div class="back">
      <h4 style="margin-top: 20px; margin-left: -1050px; color: #42b983; "></h4>
    </div>
    <div class="userData">
      <div class=" col-md-9 col-lg-9 ">
        <div class="col-xs-12 col-sm-12 col-md-6 col-lg-6 col-xs-offset-0 col-sm-offset-0 col-md-offset-3 col-lg-offset-3 toppad"  >
          <b-table-simple class="table" style="width: 500px ; height: 500px">
            <b-form-group
              label-cols-lg="8"
              label="Моето текущо здраве"
              label-size="lg"
              label-align-sm="right"
              label-class="font-weight-bold pt-0"
              class="title">
            </b-form-group>
            <b-tr>
              <b-td><b>Тегло</b></b-td>
              {{ userr.kilograms }} кг.
            </b-tr>

            <b-tr>
              <b-td><b>Височина</b></b-td>
              <b-card-text>
                {{ userr.height }} м.
              </b-card-text>
            </b-tr>

            <b-tr>
              <b-td><b>Начин на живот</b></b-td>
              <b-card-text>
                {{ userr.lifestyle.lifestyle }}
              </b-card-text>
            </b-tr>
            <b-tr>
              <b-td><b>Препоръчителен дневен прием на вода</b></b-td>
              <b-card-text>{{ recommendedWaterIntake }} мл.</b-card-text>
            </b-tr>
            <b-tr>
              <b-td><b>Препоръчителни дневни стъпки</b></b-td>
              <b-card-text>{{ recommendedSteps }} стъпки</b-card-text>
            </b-tr>
            <b-tr>
              <b-td><b>BMR</b></b-td>
              <b-card-text>{{ calculateBMR }} kcal</b-card-text>
            </b-tr>
            <b-tr>
              <b-td><b>Идеално тегло</b></b-td>
              <b-card-text>{{ calculateIdealWeight }} кг.</b-card-text>
            </b-tr>
          </b-table-simple>
        </div>
      </div>
    </div>
    <div class="chart-container">
      <Bar :data="chartData" :options="chartOptions"/>
    </div>
    <div class="chart-container">
      <Bar :data="chartDataToLoseWeight" :options="chartOptionsToLoseWeight" />
    </div>
    <div class="chart-container">
      <Bar :data="chartDataToGainWeight" :options="chartOptionsToGainWeight" />
    </div>
  </div>
</template>

<script>
import axios from 'axios'
import { Chart as ChartJS, Title, Tooltip, Legend, BarElement, CategoryScale, LinearScale } from 'chart.js'
import { Bar } from 'vue-chartjs'
import router from '@/router'
ChartJS.register(Title, Tooltip, Legend, BarElement, CategoryScale, LinearScale)
export default {
  name: 'UserView',
  components: { Bar },
  computed: {
    currentUser () {
      return this.$store.state.auth.user
    },
    calculateIdealWeight () {
      const { height, gender } = this.userr

      // Constants for J.D. Robinson calculation
      const MALE_IDEAL_WEIGHT_MULTIPLIER = 52
      const FEMALE_IDEAL_WEIGHT_MULTIPLIER = 49

      // Determine the ideal weight multiplier based on gender
      const idealWeightMultiplier =
        gender.genderType.toLowerCase() === 'male'
          ? MALE_IDEAL_WEIGHT_MULTIPLIER
          : FEMALE_IDEAL_WEIGHT_MULTIPLIER

      // Perform the ideal weight calculation
      const idealWeight = idealWeightMultiplier + 1.9 * ((height - 152.4) / 2.54)

      return Math.round(idealWeight)
    },
    calculateBMR () {
      // Destructure the needed properties from the userr object
      const { kilograms, height, age, gender } = this.userr

      // Constants for BMR calculation
      const MALE_BMR_MULTIPLIER = 5
      const FEMALE_BMR_MULTIPLIER = -161

      // Determine the BMR multiplier based on the user's gender
      const bmrMultiplier = gender.genderType.toLowerCase() === 'male' ? MALE_BMR_MULTIPLIER : FEMALE_BMR_MULTIPLIER

      // Perform the BMR calculation
      const bmr = (10 * kilograms) + (6.25 * height) - (5 * age) + bmrMultiplier

      return Math.round(bmr) // Round the BMR value for better display
    },
    recommendedWaterIntake () {
      const { kilograms, age } = this.userr
      let waterIntake

      if (age >= 16 && age <= 30) {
        waterIntake = kilograms * 35
      } else if (age >= 31 && age <= 54) {
        waterIntake = kilograms * 30
      } else if (age >= 55 && age <= 65) {
        waterIntake = kilograms * 30
      } else if (age > 65) {
        waterIntake = kilograms * 25
      } else {
        return null
      }

      return waterIntake
    },
    recommendedSteps () {
      const lifestyle = this.userr.lifestyle.lifestyle.toLowerCase()
      if (lifestyle === 'Заседнал начин на живот') {
        return 5000
      } else if (lifestyle === 'Силно активен начин на живот') {
        return 11000
      } else if (lifestyle === '1-2 тренировки седмично') {
        return 8000
      } else if (lifestyle === '3-5 тренировки седмично') {
        return 9000
      } else if (lifestyle === '6-7 тренировки седмично') {
        return 10000
      } else {
        return 8000
      }
    }
  },
  data () {
    return {
      userr: {
        id: this.$store.state.auth.user.id,
        kilograms: '',
        height: '',
        lifestyle: {
          id: '',
          lifestyle: ''
        },
        age: '',
        gender: {
          genderType: '',
          id: ''
        },
        needKcal: 0
      },
      chartData: {
        labels: [''],
        datasets: [
          {
            label: 'За ден',
            borderColor: '#42b983',
            backgroundColor: '#42b983',
            data: []
          }
        ]
      },
      chartOptions: {
        plugins: {
          title: {
            display: true,
            text: 'Препоръчителни калории за запазване на тегло',
            color: '#42b983'
          }
        },
        scales: {
          x: {
            ticks: {
              color: '#42b983'
            }
          },
          y: {
            ticks: {
              color: '#42b983'
            }
          }
        },
        elements: {
          bar: {
            backgroundColor: '#42b983'
          }
        },
        responsive: true,
        maintainAspectRatio: false,
        backgroundColor: '#42b983',
        scalesBorder: {
          x: {
            grid: {
              borderColor: '#42b983'
            }
          },
          y: {
            grid: {
              borderColor: '#42b983'
            }
          }
        }
      },
      chartDataToLoseWeight: {
        labels: ['Препоръчителни калории за сваляне на тегло'],
        datasets: [
          {
            label: 'За ден',
            borderColor: '#f4511e',
            backgroundColor: '#f4511e',
            data: []
          }
        ]
      },
      chartOptionsToLoseWeight: {
        plugins: {
          title: {
            display: true,
            text: 'Препоръчителни калории за сваляне на тегло',
            color: '#f4511e',
            font: {
              weight: 'bold'
            }
          }
        },
        scales: {
          x: {
            ticks: {
              color: '#f4511e'
            }
          },
          y: {
            ticks: {
              color: '#f4511e'
            }
          }
        }
      },
      chartDataToGainWeight: {
        labels: ['Препоръчителни калории за покачване на тегло'],
        datasets: [
          {
            label: 'Per day',
            borderColor: '#FF6384',
            backgroundColor: '#FF6384',
            data: []
          }
        ]
      },
      chartOptionsToGainWeight: {
        plugins: {
          title: {
            display: true,
            text: 'Препоръчителни калории за покачване на тегло',
            color: '#FF6384'
          }
        },
        scales: {
          x: {
            ticks: {
              color: '#FF6384'
            }
          },
          y: {
            ticks: {
              color: '#FF6384'
            }
          }
        }
      }
    }
  },
  mounted () {
    this.fetchUser()
  },
  methods: {
    calculateRecommendedCaloriesToGainWeight () {
      const calculateRecommendedCaloriesToGainWeight = this.userr.needKcal + 300
      console.log(calculateRecommendedCaloriesToGainWeight)
      return calculateRecommendedCaloriesToGainWeight
    },
    async fetchChartDataToGainWeight () {
      const needKcalToGainWeight = Math.floor(this.calculateRecommendedCaloriesToGainWeight())

      this.$set(this.chartDataToGainWeight.datasets[0], 'data', [needKcalToGainWeight])

      this.chartDataToGainWeight = {
        labels: ['Препоръчителни калории за покачване на тегло'],
        datasets: [
          {
            label: 'За ден',
            backgroundColor: '#FF6384',
            data: [needKcalToGainWeight]
          }
        ]
      }
    },
    calculateRecommendedCaloriesToLoseWeight () {
      const calculateRecommendedCaloriesToLoseWeight = this.userr.needKcal - 300
      console.log(calculateRecommendedCaloriesToLoseWeight)
      return calculateRecommendedCaloriesToLoseWeight
    },
    async fetchChartDataToLoseWeight () {
      const needKcalToLoseWeight = Math.floor(this.calculateRecommendedCaloriesToLoseWeight())

      this.$set(this.chartDataToLoseWeight.datasets[0], 'data', [needKcalToLoseWeight])

      this.chartDataToLoseWeight = {
        labels: ['Препоръчителни калории за сваляне на тегло'],
        datasets: [
          {
            label: 'За ден',
            backgroundColor: '#f4511e',
            data: [needKcalToLoseWeight]
          }
        ]
      }
    },
    setData (response) {
      this.userr = response.data
    },
    async fetchUser () {
      if (!this.currentUser || !this.currentUser.id) {
        return
      }

      const API_URL = 'http://localhost:8080/app'
      try {
        const response = await axios.get(API_URL + '/user', {
          params: {
            id: this.currentUser.id
          }
        })

        if (response && response.data) {
          this.userr = response.data
          console.log('User data fetched successfully:', response.data)
          await this.fetchChartData() // Call fetchChartData
          await this.fetchChartDataToLoseWeight()
          await this.fetchChartDataToGainWeight()
        } else {
          alert('Моля, въведете данни в изгледа за профил.')
          await router.push('/profile')
        }
      } catch (error) {
        alert('Something went wrong')
        console.error('Error fetching user data:', error.response.data)
      }
    },
    async fetchChartData () {
      const API_URL = 'http://localhost:8080/app'
      let needKcal = 0
      needKcal = Math.floor(this.calculateRecommendedCalories())
      console.log('kcal:', needKcal)

      try {
        const response = await axios.post(API_URL + '/saveNeedKcal', {
          id: this.currentUser.id,
          needKcal: needKcal
        })

        console.log('NeedKcal data saved successfully:', response.data)

        this.chartData = {
          labels: ['Препоръчителни калории за запазване на тегло'],
          datasets: [
            {
              label: 'За ден',
              backgroundColor: '#42b983',
              data: [needKcal]
            }
          ]
        }
      } catch (error) {
        console.error('Error saving NeedKcal data:', error.response.data)
      }
    },
    calculateRecommendedCalories () {
      const { kilograms, height, age, gender, lifestyle } = this.userr

      // Constants for Mifflin-St. Jeor equation
      const MALE_BMR_MULTIPLIER = 5
      const FEMALE_BMR_MULTIPLIER = -161
      const HEIGHT_MULTIPLIER = 6.25
      const WEIGHT_MULTIPLIER = 10
      const AGE_MULTIPLIER = 5

      // Determine the BMR multiplier based on gender
      const bmrMultiplier =
        gender.genderType.toLowerCase() === 'male'
          ? MALE_BMR_MULTIPLIER
          : FEMALE_BMR_MULTIPLIER

      // Calculate the BMR using the Mifflin-St. Jeor equation
      const bmr =
        WEIGHT_MULTIPLIER * kilograms +
        HEIGHT_MULTIPLIER * height -
        AGE_MULTIPLIER * age +
        bmrMultiplier

      // Apply the lifestyle multiplier
      let lifestyleMultiplier
      const lifestyleValue = lifestyle.lifestyle.toLowerCase()

      if (lifestyleValue === 'Силно активен начин на живот') {
        lifestyleMultiplier = 1.9
      } else if (lifestyleValue === 'Заседнал начин на живот') {
        lifestyleMultiplier = 1.4
      } else if (lifestyleValue === '1-2 тренировки седмично') {
        lifestyleMultiplier = 1.375
      } else if (lifestyleValue === '3-5 тренировки седмично') {
        lifestyleMultiplier = 1.55
      } else if (lifestyleValue === '6-7 тренировки седмично') {
        lifestyleMultiplier = 1.725
      } else {
        return null
      }

      const recommendedCalories = Math.round(bmr * lifestyleMultiplier)

      return recommendedCalories
    }
  }
}
</script>

<style scoped>
.back{
  background-image: url('../assets/healthy.jpg');
  background-position: center;
  background-size: cover;
  width: 100%;
  height: 100%;
  object-fit: contain;
  position: fixed;
  top: 0px;
  margin-top: 70px;
  left: 0px;
  z-index: 0;
}
.table{
  background-image: url('../assets/profileTable.jpg');
  background-position: center;
  background-size: cover;
  width: 100px;
  height: 100px;
  margin-top: -95px;
  margin-left: 570px;
  /*border-style: solid;*/
  /*border-color: #42b983;*/
  border-radius: 25px;
  /*border-width: 2px;*/
}
.toppad {
  margin-top: 50px;
}
.userData{
  margin-top: 70px;
  margin-left: -800px;
  border-radius: 25px;
}
.title {
  color: #42b983;
  /*margin-bottom: 10px;*/
  margin-left: 60px;
}
.chart-container {
  margin-top: 200px;
  color: #42b983;
  /*display: flex;*/
  align-items: center;
  justify-content: center;
  z-index: 1;
  /*position: relative;*/
  height: 100%;
  width: 100%;
}
</style>
