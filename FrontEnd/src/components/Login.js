import React, { useState, useEffect } from 'react'
import '../Login.css'
import HomeIcon from '@material-ui/icons/Home'
import { Link } from 'react-router-dom'
import { Form, Button } from 'react-bootstrap'
import Logo from '../assets/img/Logo.png'
import { useNavigate } from 'react-router-dom'
import authService from '../services/authService'
import httpService from '../services/httpService'
import { toast } from 'react-toastify'

function LoginIn() {
  const history = useNavigate()
  const [inputs, setInputs] = useState({
    email: '',
    password: '',
    authority: '',
  })
  const [isAuthenticated, setIsAuthenticated] = useState(false)
  const [authorities, setAuthorities] = useState([])
  const [isError, setIsError] = useState(false)
  const [isFormValid, setIsFormValid] = useState(false)
  const [isCheckTerms, setIsCheckTerms] = useState(false)
  const [inputElements, setInputElements] = useState({
    email: {
      validation: {
        required: true,
        isEmail: true,
      },
      valid: false,
      touched: false,
      invalidText: 'Invalid Email',
    },
    password: {
      validation: {
        required: true,
        minLength: 1,
      },
      valid: false,
      touched: false,
      invalidText: 'Please enter password',
    },
  })
  const checkValidity = (value, rules) => {
    let isValid = true
    if (!rules) {
      return true
    }

    if (rules.required) {
      isValid = value.trim() !== '' && isValid
    }

    if (rules.minLength) {
      isValid = value.length >= rules.minLength && isValid
    }

    if (rules.maxLength) {
      isValid = value.length <= rules.maxLength && isValid
    }

    if (rules.isEmail) {
      const pattern = /[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?/
      isValid = pattern.test(value) && isValid
    }

    if (rules.isNumeric) {
      const pattern = /^\d+$/
      isValid = pattern.test(value) && isValid
    }

    return isValid
  }
  useEffect(() => {
    let formIsValid = true
    for (let inputIdentifier in inputElements) {
      formIsValid = inputElements[inputIdentifier].valid && formIsValid
    }
    setIsFormValid(formIsValid)
  }, [inputs])

  const { email, password, authority } = inputs

  async function handleSelectChange(e) {
    e.stopPropagation()
    setIsError(false)
    const { name, value } = e.target
    setInputs((inputs) => ({ ...inputs, [name]: value }))
  }

  async function handleChange(e) {
    e.stopPropagation()
    setIsError(false)
    const { name, value } = e.target
    setInputs((inputs) => ({ ...inputs, [name]: value }))
    if (
      checkValidity(e.target.value, inputElements[e.target.name].validation)
    ) {
      setInputElements({
        ...inputElements,
        [e.target.name]: {
          ...inputElements[e.target.name],
          touched: true,
          valid: true,
        },
      })
    } else {
      setInputElements({
        ...inputElements,
        [e.target.name]: {
          ...inputElements[e.target.name],
          touched: true,
          valid: false,
        },
      })
    }
  }
  const signIn = async (e) => {
    setIsError(false)
    e.preventDefault()
    try {
      let data = await authService.login(email, password)
      setIsAuthenticated(true)
      const user = authService.getCurrentUser()
      if (user && user.authorities) {
        const listOfAuths = user.authorities.split(',')
        setAuthorities(listOfAuths)
        setInputs({ authority: listOfAuths[0] })
      }
    } catch (ex) {
      if (ex.response && ex.response.status === 400) {
        const errors = { ...this.state.errors }
        errors.username = ex.response.data
        this.setState({ errors })
      }
    }
  }

  const continueToHomePage = (e) => {
    e.preventDefault()
    toast('Logged in as ' + authority)
    if (authority === 'VENDOR') window.location.href = '/vendor'
    else if (authority === 'ADMIN') window.location.href = '/viewproducts'
    else history('/')
  }

  return (
    <div className="login">
      <Link to="/">
        <img className="login_img" src={Logo} alt="logo" />
      </Link>
      <div className="login_container">
        <h1>User Sign-in</h1>
        {!isAuthenticated ? (
          <React.Fragment>
            <form>
              <Form.Group className="mb-" controlId="formBasicEmail">
                <Form.Label>Email address</Form.Label>
                <Form.Control
                  type="email"
                  placeholder="Enter email"
                  name="email"
                  value={email}
                  onChange={(e) => handleChange(e)}
                />
                <Form.Text className="text-muted">
                  We'll never share your email with anyone else.
                </Form.Text>
                {!inputElements.email.valid && inputElements.email.touched && (
                  <div style={{ color: 'red' }}>
                    {' '}
                    {inputElements.email.invalidText}
                  </div>
                )}
              </Form.Group>
              <Form.Group className="mb-3" controlId="formBasicEmail">
                <Form.Label>Password</Form.Label>
                <Form.Control
                  type="password"
                  placeholder="Enter password"
                  name="password"
                  value={password}
                  onChange={(e) => handleChange(e)}
                />
              </Form.Group>
              <Form.Group className="mb-3" controlId="formBasicCheckbox">
                <Form.Check
                  type="checkbox"
                  label="By signing in you will agree all the terms & condition by FIRST CHOICE"
                  onChange={() => setIsCheckTerms(!isCheckTerms)}
                />
              </Form.Group>
              {isError && (
                <Form.Text style={{ color: 'red' }}>
                  Incorrect Email and Password
                </Form.Text>
              )}
              <button
                disabled={!(isFormValid && isCheckTerms)}
                className={
                  isFormValid && isCheckTerms ? 'innerbutton' : 'inactivebtn'
                }
                type="submit"
                onClick={(e) => signIn(e)}
              >
                Sign In
              </button>
              <br />
            </form>
            <br />
            <Link to="/register">
              <button className="innerbutton"> Create Account</button>
            </Link>
          </React.Fragment>
        ) : (
          <Form>
            <Form.Group>
              <Form.Select
                aria-label="Default select example"
                name="authority"
                onChange={(e) => handleSelectChange(e)}
              >
                {authorities.map((a) => (
                  <option key={a} value={a}>
                    {a}
                  </option>
                ))}
              </Form.Select>
              <button
                type="submit"
                className="innerbutton"
                onClick={(e) => continueToHomePage(e)}
              >
                Continue
              </button>
            </Form.Group>
          </Form>
        )}
      </div>
    </div>
  )
}
export default LoginIn
