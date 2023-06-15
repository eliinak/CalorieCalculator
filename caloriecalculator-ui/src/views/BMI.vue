<template>
  <div class="profileformBMI">
    <div class="tableBMI">
      <h2 style="color: #42b983;">Изчисляване на BMI</h2>
      <div>
        <label for="height">Височина (см.):</label>
        <input type="number" id="height" v-model="height" :disabled="isHeightDisabled" />
      </div>
      <div>
        <label for="weight">Тегло (кг.):</label>
        <input type="number" id="weight" v-model="weight" :disabled="isWeightDisabled" />
      </div>
      <div>
        <label for="bodyType" style="color: #42b983">Изберете вашия тип тяло:</label>
        <div class="select-wrapper">
          <select id="bodyType" v-model="bodyType">
            <option value="ectomorph">Ектоморф</option>
            <option value="mesomorph">Мезоморф</option>
            <option value="endomorph">Ендоморф</option>
          </select>
        </div>
      </div>
      <button @click="calculateBMI" class="calculate-button" style="z-index: 2">Изчисли BMI</button>
      <table v-if="bmi" class="bmi-table">
        <thead>
        <tr>
          <th>Твоят BMI</th>
          <th>BMI Категория</th>
        </tr>
        </thead>
        <tbody>
        <tr>
          <td>{{ bmi.toFixed(2) }}</td>
          <td>{{ getBMICategory() }}</td>
        </tr>
        </tbody>
      </table>
      <div v-if="bodyTypeDescription" class="body-type-description">
        <h3>Описание на типа тяло</h3>
        <p>{{ bodyTypeDescription }}</p>
      </div>
      <div class="image-container">
        <img src="../assets/Body-Types.jpg" alt="Image" class="image" />
      </div>
    </div>
    <div class="text-container">
      <h3>Какво е BMI?</h3>
      <p class="marked">
        BMI означава индекс на телесна маса. Това е мярка за телесното тегло на човек спрямо неговия ръст,тегло и тип тяло.
        BMI се изчислява, като теглото на човек в килограми се раздели на квадрата на височината му в метри. След това изваждаме или прибавяме
        единица в зависимост от типа тяло.
        Обикновено се използва като индикатор за това дали човек има здравословно, поднормено или надномерно телесно тегло или затлъстяване.
      <p>
        Поднормено: BMI по-малко от 18.5
      </p>
      <p>
        Нормално: BMI между 18.5 и 24.9
      </p>
      <p>
        Наднормено: BMI между 25 и 29.9
      </p>
      <p>
        Затлъстяване: BMI 30 или повече
      </p>
    </div>
  </div>
</template>

<script>
import axios from 'axios'

export default {
  name: 'BMI',
  data () {
    return {
      height: '',
      weight: '',
      bmi: null,
      bodyType: '',
      bodyTypeDescription: '',
      isHeightDisabled: true,
      isWeightDisabled: true,
      isBMIEnabled: true
    }
  },
  computed: {
    currentUser () {
      return this.$store.state.auth.user
    }
  },
  mounted () {
    this.fetchUser()
  },
  methods: {
    fetchUser () {
      if (!this.currentUser || !this.currentUser.id) {
        return
      }

      const API_URL = 'http://localhost:8080/app'
      axios
        .get(API_URL + '/user', {
          params: {
            id: this.currentUser.id
          }
        })
        .then(response => {
          if (response && response.data) {
            this.height = response.data.height
            this.weight = response.data.kilograms
            console.log(response)
            if (!this.height || !this.weight) {
              alert('Моля, въведете данни в изгледа за профил.')
              this.$router.push('/profile')
            }
          } else {
            alert('Моля, въведете данни в изгледа за профил.')
            this.$router.push('/profile')
          }
        })
        .catch(error => {
          alert('Something went wrong')
          console.error(error.response.data)
        })
    },
    calculateBMI () {
      if (!this.height || !this.weight) {
        return
      }

      const heightInMeters = this.height / 100
      const bmi = this.weight / (heightInMeters * heightInMeters)

      if (this.bodyType === 'ectomorph') {
        this.bmi = bmi - 1
      } else if (this.bodyType === 'mesomorph') {
        this.bmi = bmi
      } else if (this.bodyType === 'endomorph') {
        this.bmi = bmi + 1
      }

      this.setBodyTypeDescription()
    },
    getBMICategory () {
      if (!this.bmi) return ''

      if (this.bmi < 18.5) {
        return 'Поднормено тегло'
      } else if (this.bmi < 25) {
        return 'Нормално тегло'
      } else if (this.bmi < 30) {
        return 'Наднормено тегло'
      } else {
        return 'Затлъстяване'
      }
    },
    setBodyTypeDescription () {
      if (!this.bodyType) return ''

      if (this.bodyType === 'ectomorph') {
        this.bodyTypeDescription = 'Ектоморфите обикновено са слаби с бърз метаболизъм и им е трудно да наддават на тегло или мускулна маса.'
      } else if (this.bodyType === 'mesomorph') {
        this.bodyTypeDescription = 'Мезоморфите имат пропорционално тяло с добра мускулна структура и им е по-лесно да изграждат и поддържат мускулна маса.'
      } else if (this.bodyType === 'endomorph') {
        this.bodyTypeDescription = 'Ендоморфите обикновено имат по-висок процент телесни мазнини, по-бавен метаболизъм и може да им е по-лесно да наддават на тегло и мускулна маса.'
      }
    }
  }
}
</script>

<style>
.profileformBMI {
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
  display: flex;
}

.tableBMI {
  background-image: url('../assets/profileTable.jpg');
  background-position: center;
  background-size: cover;
  width: 1400px;
  height: 450px;
  margin-top: 35px;
  margin-left: 50px;
  border-radius: 25px;
}

.bmi-table {
  margin-top: 20px;
  margin-left: 110px;
  border-radius: 20px;
}

.bmi-table th,
.bmi-table td {
  padding: 8px;
  text-align: center;
}

.bmi-table th {
  background-color: #42b983;
  color: white;
}

.bmi-table tr:nth-child(even) {
  background-color: #f2f2f2;
}

.body-type-description {
  margin-top: 20px;
}

.body-type-description h3 {
  color: #42b983;
  font-size: 18px;
  font-weight: bold;
}

.body-type-description p {
  color: #42b983;
}

.select-wrapper {
  position: relative;
  z-index: 1;
}

.calculate-button {
  z-index: 2;
  background-color: #42b983;
  border-radius: 5px;
}
.image-container {
  top: 380px;
  left: 690px;
  position: fixed;
}
.marked {
  background-color: azure;
}
.text-container {
  margin-top: 10px;
}
</style>
