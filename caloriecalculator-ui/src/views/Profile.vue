<template>
  <div class="profileform">
    <b-card class="form">
      <b-form-group
        label-cols-lg="8"
        label="Редактиране на профил"
        label-size="lg"
        label-align-sm="right"
        label-class="font-weight-bold pt-0"
        class="title">
      </b-form-group>

      <b-form-group
        label-for="name"
        class="firstname">
        <b-input-group prepend="Име"  class="name">
          <b-form-input type="text" id="name" v-model="filters.name" @keypress="onlyLetters">
          </b-form-input>
        </b-input-group>
      </b-form-group>

      <b-form-group label-for="lastname"
                    class="lastname">
        <b-input-group prepend="Фамилия" class="lastname" >
          <b-form-input type="text" id="lastname" v-model="filters.surname" @keypress="onlyLetters"></b-form-input>
        </b-input-group>
      </b-form-group>

      <b-form-group label-for="age"
                    class="age">
        <b-input-group prepend="Години" class="age" >
          <b-form-input type="number" id="age" v-model="filters.age"></b-form-input>
        </b-input-group>
      </b-form-group>

      <b-form-group label-for="height"
                    class="height">
        <b-input-group prepend="Височина" class="height" >
          <b-form-input type="number" id="height" v-model="filters.height"></b-form-input>
        </b-input-group>
      </b-form-group>

      <b-form-group label-for="kilograms"
                    class="kilograms">
        <b-input-group prepend="Тегло" class="kilograms" >
          <b-form-input type="number" id="kilograms" v-model="filters.kilograms"></b-form-input>
        </b-input-group>
      </b-form-group>

      <b-form-group label-for="email" class="email">
        <b-input-group prepend="e-mail" class="mail">
          <b-form-input
            name="email"
            id="email"
            v-validate="'required|email|max:50'"
            type="email"
            v-model="filters.email"
          ></b-form-input>
        </b-input-group>
      </b-form-group>
      <div v-if="submitted && errors.has('email')" class="alert-danger">
        {{ errors.first('email') }}
      </div>
      <div v-if="invalidEmail" class="alert-danger">
        Въведения email е невалиден или съществува!
      </div>

      <b-form-group
        label-cols-sm="4"
        label=""
        label-align-sm="right"
        class="lifestyle"
      >
        <label for="lifestyle">Начин на живот:</label>
        <b-form-select
          id="lifestyle"
          v-model="filters.lifestyle"
          :options="lifestyleOptions"
        ></b-form-select>
      </b-form-group>

      <b-form-group
        label-cols-sm="1"
        label=""
        label-align-sm="right"
        class="gender">
        <b-form-radio-group
          id="gender" v-model="filters.gender"
          :options="genderOptions"
          name="radio-inline">
        </b-form-radio-group>
      </b-form-group>

      <b-button-group>
        <b-button class="button" v-on:click="saveUser">Запази</b-button>
      </b-button-group>
      <b-button-group>
        <b-button class="button" v-on:click="fetchUser">Виж Моите Данни</b-button>
      </b-button-group>
    </b-card>
    <div class="text-container">
      <img src="../assets/tips-to-beat-a-sedentary-lifestyle.jpg" alt="Image" class="image" />
    </div>
  </div>
</template>

<script>
import ProfileService from '@/services/profile-service'
import axios from 'axios'

export default {
  name: 'ProfileTab',
  computed: {
    currentUser () {
      return this.$store.state.auth.user
    }
  },

  mounted () {
    if (!this.currentUser) {
      this.$router.push('/')
    }
  },

  data () {
    return {
      invalidEmail: false,
      submitted: false,
      genderOptions: [
        { text: 'Мъж', value: 'male' },
        { text: 'Жена', value: 'female' }
      ],
      lifestyleOptions: [
        { text: 'Заседнал начин на живот', value: 'Заседнал начин на живот' },
        { text: 'Силно активен начин на живот', value: 'Силно активен начин на живот' },
        { text: '1-2 тренировки седмично', value: '1-2 тренировки седмично' },
        { text: '3-5 тренировки седмично', value: '3-5 тренировки седмично' },
        { text: '6-7 тренировки седмично', value: '6-7 тренировки седмично' }
      ],
      filters: {
        id: this.$store.state.auth.user.id,
        name: '',
        surname: '',
        age: '',
        email: '',
        gender: '',
        height: '',
        kilograms: '',
        lifestyle: ''
      },
      currentEmail: ''
    }
  },

  methods: {
    onlyLetters (event) {
      const charCode = event.keyCode || event.which
      const char = String.fromCharCode(charCode)
      if (!/^[A-Za-zА-Яа-я]+$/.test(char)) {
        event.preventDefault()
      }
    },
    isValidEmail (email) {
      const emailPattern = /^[^\s@]+@[^\s@]+\.[^\s@]+$/

      return emailPattern.test(email.trim())
    },
    saveUser () {
      const email = this.filters.email
      console.log(email)
      if (email === this.$store.state.auth.user.email) {
        ProfileService.saveData(this.filters)
          .then(response => {
            if (response && response.data) {
              const responseData = response.data
              this.filters = responseData
              console.log(response)
              console.log(this.filters)
            } else {
              alert('No response data received')
            }
          })
          .catch(error => {
            this.msg =
              (error.response && error.response.data) ||
              error.message ||
              error.toString()
            console.error('Error saving user data:', error.response?.data)
          })
      } else {
        const isValidEmail = this.isValidEmail(email)
        if (!isValidEmail) {
          this.invalidEmail = true
          return
        }

        ProfileService.checkEmailExists(email)
          .then(response => {
            const exists = response.data

            if (exists) {
              this.invalidEmail = true
            } else {
              this.invalidEmail = false

              ProfileService.saveData(this.filters)
                .then(response => {
                  if (response && response.data) {
                    const responseData = response.data
                    this.filters = responseData
                    console.log(response)
                    console.log(this.filters)
                  } else {
                    alert('No response data received')
                  }
                })
                .catch(error => {
                  this.msg =
                    (error.response && error.response.data) ||
                    error.message ||
                    error.toString()
                  alert('Something went wrong')
                  console.error('Error saving user data:', error.response?.data)
                })
            }
          })
          .catch(error => {
            console.error('Error checking email existence:', error)
            alert('Something went wrong')
          })
      }
    },
    fetchUser () {
      if (!this.currentUser || !this.currentUser.id) {
        return
      }
      console.log(this.$store.state.auth.user)
      console.log(this.$store.state.auth.user.email)

      const API_URL = 'http://localhost:8080/app'
      axios
        .get(API_URL + '/user', {
          params: {
            id: this.currentUser.id
          }
        })
        .then(response => {
          if (response && response.data) {
            this.filters = response.data
          }
          this.currentEmail = this.$store.state.auth.user.email
          this.filters.email = this.$store.state.auth.user.email
          console.log(response)
        })
        .catch(error => {
          alert('There is no data for the current user')
          console.error(error.response.data)
        })
    }
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
  display: flex;
}
.form{
  background-image: url('../assets/profileTable.jpg');
  background-position: center;
  background-size: cover;
  width: 550px;
  height: 590px;
  margin-right: 750px;
  margin-left: 10px;
  margin-bottom: 50px;
  margin-top: 20px;
  border-style: solid;
  /*border-color: #42b983;*/
  border-radius: 25px;
  border-width: 2px;
}
.title{
  color: #42b983;
  size: auto;
  margin-left: 76px;
}
.button{
  background-color: #42b983;
}
.gender{
  margin-left: -10px;
}
.lifestyle{
  margin-left: -250px;
}
.text-container {
  flex: 1;
  padding: 5px;
  margin-left: -700px;
  font-weight: bold;
  color: #42b983;
}
.marked-text {
  background-color: azure;
}
</style>
