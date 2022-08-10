import Vue from 'vue'
import Vuex from 'vuex'
import axios from 'axios'

Vue.use(Vuex)

/*
 * The authorization header is set for axios when you login but what happens when you come back or
 * the page is refreshed. When that happens you need to check for the token in local storage and if it
 * exists you should set the header so that it will be attached to each request
 */
const currentToken = localStorage.getItem('token')
const currentUser = JSON.parse(localStorage.getItem('user'));

if (currentToken != null) {
  axios.defaults.headers.common['Authorization'] = `Bearer ${currentToken}`;
}

export default new Vuex.Store({
  state: {
    token: currentToken || '',
    user: currentUser || {
      email: 'carly@user.com',
      imageURL: 'https://media-exp1.licdn.com/dms/image/C5603AQG11wYnSQ28ug/profile-displayphoto-shrink_800_800/0/1625681037290?e=1665619200&v=beta&t=YYcwiDfDxqJeZNyfPrxqKQMP5_9yZnLeiY1LxM85JfI'
    }, //ADDED email property to empty user object to test mutation. Can be removed if necessary, just be sure to comment out UPDATE_USER_EMAIL mutation.
    dietaryRestrictions: [
      {
        id: 1,
        name: "Dairy-free",
        abbreviation: "df",
        active: true
      }, {
        id: 2,
        name: "Gluten-free",
        abbreviation: "gf",
        active: false
      }, {
        id: 3,
        name: "Halal",
        abbreviation: "hl",
        active: false
      }, {
        id: 4,
        name: "Kosher",
        abbreviation: "k",
        active: false
      }, {
        id: 5,
        name: "Low-carb",
        abbreviation: "lc",
        active: false
      }, {
        id: 6,
        name: "Nut-free",
        abbreviation: "nf",
        active: true
      }, {
        id: 7,
        name: "Vegan",
        abbreviation: "vn",
        active: false
      }, {
        id: 8,
        name: "Vegetarian",
        abbreviation: "v",
        active: false
      },

    ],
  },
  mutations: {
    SET_AUTH_TOKEN(state, token) {
      state.token = token;
      localStorage.setItem('token', token);
      axios.defaults.headers.common['Authorization'] = `Bearer ${token}`
    },
    SET_USER(state, user) {
      state.user = user;
      localStorage.setItem('user', JSON.stringify(user));
    },
    LOGOUT(state) {
      localStorage.removeItem('token');
      localStorage.removeItem('user');
      state.token = '';
      state.user = {};
      axios.defaults.headers.common = {};
    },
    // SET_USER_DIETARY_RESTRICTIONS(state, dietaryRestrictions) {
    //   state.dietaryRestrictions = dietaryRestrictions;
    // },
    UPDATE_DIETARY_RESTRICTIONS(state, selectedItems) {
      state.dietaryRestrictions.forEach(r => {
        if (selectedItems.includes(r.id)) {
          r.active = true;
        }
      });
    },
    UPDATE_USER_EMAIL(state, userEmail) {
      state.user.email = userEmail;
      console.log('UPDATE_USER_EMAIL');
    },
    UPDATE_USER_IMAGE_URL(state, userImageURL) {
      state.user.imageURL = userImageURL;
      console.log('UPDATE_USER_IMAGE_URL');
    }
  }
})
