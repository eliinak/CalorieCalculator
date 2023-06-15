<template>
  <div id="app">
    <nav class="navbar navbar-expand navbar-dark  navbar-custom" style="background-color: #42b983;">
      <a href class="navbar-brand" @click.prevent>Моят здравословен калкулатор</a>
      <div class="navbar-nav mr-auto">
        <li v-if="showAdminBoard" class="nav-item">
          <router-link to="/admin" class="nav-link">Admin Board</router-link>
        </li>
        <li class="nav-item">
          <router-link v-if="currentUser" to="/user" class="nav-link">Моето здраве</router-link>
        </li>
      </div>

      <div v-if="!currentUser" class="navbar-nav ml-auto">
        <li class="nav-item">
          <router-link to="/register" class="nav-link">
            <font-awesome-icon icon="user-plus" />
            Регистрация
          </router-link>
        </li>
        <li class="nav-item">
          <router-link to="/" class="nav-link">
            <font-awesome-icon icon="sign-in-alt" />
            Вход
          </router-link>
        </li>
      </div>

      <div v-if="currentUser" class="navbar-nav ml-auto">
        <li class="nav-item">
          <router-link to="/profile" class="nav-link">
            <font-awesome-icon icon="user" />
            {{ currentUser.username }}
          </router-link>
        </li>
        <li class="nav-item">
          <router-link to="/myfood" class="nav-link">Моите ястия</router-link>
        </li>
        <li class="nav-item">
          <router-link to="/bmi" class="nav-link">BMI</router-link>
        </li>
        <li class="nav-item">
          <a class="nav-link" href @click.prevent="logOut">
            <font-awesome-icon icon="sign-out-alt" />Излез
          </a>
        </li>
      </div>

    </nav>

    <div class="container">
      <router-view />
    </div>
  </div>
</template>
<script>
export default {
  computed: {
    currentUser () {
      return this.$store.state.auth.user
    },
    showAdminBoard () {
      if (this.currentUser && this.currentUser.roles) {
        return this.currentUser.roles.includes('ROLE_ADMIN')
      }

      return false
    }
  },
  methods: {
    logOut () {
      this.$store.dispatch('auth/logout')
      this.$router.push('/')
    }
  }
}
</script>
<style>
#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #42b983;
}

#nav {
  padding: 30px;
}

#nav a {
  font-weight: bold;
  color: #42b983;
}

#nav a.router-link-exact-active {
  color: #42b983;
}
.navbar{
  line-height: 70px;
  height: 70px;
  padding-top: 0;
}
.navbar-custom{
  background-color: #42b983;
}
</style>
