<template>
  <div class="photoContainer">
    <div class="registerform">
      <form name="form" class="text-center border border-light p-5" @submit.prevent="handleRegister">
        <div v-if="!successful">
          <div class="form-group">
            <p style="color:#42b983;" class="h4 mb-4">Направете своята регистрация</p>
            <input
              placeholder="Потребителско име"
              v-model="user.username"
              v-validate="'required|min:3|max:20'"
              type="text"
              class="form-control"
              name="username"
              id="username"
            />
            <div
              v-if="submitted && errors.has('username')"
              class="alert-danger"
            >Невалидно потребителско име или вече съществуващо</div>
          </div>
          <div class="form-group">
            <input
              placeholder="Email"
              v-model="user.email"
              v-validate="'required|email|max:50'"
              type="email"
              class="form-control"
              name="email"
              id="email"
            />
            <div
              v-if="submitted && errors.has('email')"
              class="alert-danger"
            >Невалиден email или вече съществуващ</div>
          </div>
          <div class="form-group">
            <input
              placeholder="Парола"
              v-model="user.password"
              v-validate="'required|min:6|max:40'"
              type="password"
              class="form-control"
              name="password"
              id="password"
            />
            <div
              v-if="submitted && errors.has('password')"
              class="alert-danger"
            >Паролата трябва да съдържа поне 6 символа</div>
          </div>
          <div class="form-group">
            <button class="btn btn-info btn-block my-4 registerButton" :disabled="loading" type="submit" >
              <span v-show="loading" class="spinner-border spinner-border-sm"></span>
              <span>Регистрация</span>
            </button>
          </div>
        </div>
      </form>
      <div
        v-if="message"
        class="alert"
        :class="successful ? 'alert-success' : 'alert-danger'"
      >{{ message }}</div>
    </div>
  </div>
</template>

<script>
import User from '../models/user'

export default {
  name: 'RegisterView',
  data () {
    return {
      user: new User('', '', ''),
      submitted: false,
      successful: false,
      message: '',
      loading: false
    }
  },
  computed: {
    loggedIn () {
      return this.$store.state.auth.status.loggedIn
    }
  },
  methods: {
    handleRegister () {
      this.loading = true
      this.message = ''
      this.submitted = true
      this.$validator.validate().then((isValid) => {
        this.loading = false
        if (isValid) {
          this.loading = true
          this.$store.dispatch('auth/register', this.user).then(
            (data) => {
              this.message = data.message
              this.successful = true
            },
            (error) => {
              this.loading = false
              this.message =
                (error.response && error.response.data) ||
                error.message ||
                error.toString()
              this.successful = false
            }
          )
        }
      })
    },
    translateErrorMessage (errorMessage) {
      const translations = {
        'The {field} field is required.': 'Полето "{field}" е задължително.',
        'The {field} field must be at least {length} characters.': 'Полето "{field}" трябва да е поне {length} символа.',
        'The {field} field may not be greater than {length} characters.': 'Полето "{field}" не може да е по-голямо от {length} символа.'
      }

      const fieldMatches = errorMessage.match(/{([^{}]+)}/)
      if (fieldMatches && fieldMatches.length > 1) {
        const field = fieldMatches[1]
        const translation = translations[errorMessage]
        if (translation) {
          return translation.replace('{field}', this.getFieldTranslation(field))
        }
      }

      return errorMessage
    },
    getFieldTranslation (field) {
      const fieldTranslations = {
        username: 'Потребителско име',
        email: 'Email',
        password: 'Парола'
      }

      return fieldTranslations[field] || fieldTranslations[field.toLowerCase()] || field
    }
  }
}
</script>

<style scoped>
.registerform {
  max-width: 700px;
  margin: 0 auto;
  margin-top: 100px;
  border: #42b983;
  border-style: groove;
}

.photoContainer {
  background-image: url('~@/assets/background.jpg');
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
}

.registerButton {
  background-color: #42b983;
  border: #42b983;
}
</style>
