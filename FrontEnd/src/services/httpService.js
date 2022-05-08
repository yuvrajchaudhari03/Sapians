import axios from 'axios'
import 'react-toastify/dist/ReactToastify.css'
import { toast } from 'react-toastify'

axios.interceptors.response.use(null, (error) => {
  const expectedError =
    error.response &&
    error.response.status >= 400 &&
    error.response.status < 500

  try {
    var msg = error.response.data.message
  } catch (e) {
    console.log('Mesage not found')
  }

  if (!expectedError) {
    toast.error('An unexpected error occurrred.' + msg)
  }

  if (error.response && error.response.status == 401) {
    localStorage.setItem('token', '')
    toast.error('Login Failed. Please Login again ' + msg)
  }
  return Promise.reject(error)
})

function setJwt(jwt) {
  axios.defaults.headers.common['Authorization'] = jwt
}

export default {
  get: axios.get,
  post: axios.post,
  put: axios.put,
  delete: axios.delete,
  patch: axios.patch,
  setJwt,
}
