import React from 'react'
import '../register.css'
import { Link } from 'react-router-dom'
import 'react-dropdown/style.css'
import HomeIcon from '@material-ui/icons/Home'
import { Container, Form } from 'react-bootstrap'
import Logo from '../assets/img/Logo.png'
import httpService from '../services/httpService'
import authService from '../services/authService'

export default class AddMoney extends React.Component {
  constructor(props) {
    super(props)
    this.state = {
      cname: '',
      sign: authService.getCurrentUser(),
    }
  }
  handleChange1 = (a) => {
    this.setState({ cname: a.target.value })
  }
  submitForm = async (e) => {
    e.preventDefault()
    // const reqData = {
    //   method: 'POST',
    //   headers: {
    //     'Content-Type': 'application/json',
    //   },
    //   body: JSON.stringify({
    //     u_id: this.state.sign.u_id,
    //     wallet: Number(this.state.cname),
    //   }),
    // }
    // const url = process.env.REACT_APP_BASE_URL + '/user/addMoney'
    // await fetch(url, reqData)
    //   .then((response) => response.json())
    //   .then((data) => {
    //     let sign = JSON.parse(localStorage.getItem('data1'))
    //     sign.wallet = data.wallet
    //     localStorage.setItem('data1', JSON.stringify(sign))
    //     window.location.href = '/wallet'
    //   })
    //   .catch((error) => {
    //     console.log(error)
    //   })

    const url =
      process.env.REACT_APP_BASE_URL +
      '/user/addMoney/' +
      Number(this.state.cname)
    try {
      await httpService.post(url)
      //   this.props.history.push('/wallet')
      window.location.href = '/wallet'
    } catch (e) {
      console.log(e)
      //   this.props.history.push('/login')
    }
  }

  render() {
    return (
      <Container>
        <div className="register">
          <div style={{ textAlign: 'center' }}>
            <Link to="/">
              <img className="login_img" src={Logo} alt="logo" />
            </Link>
          </div>
          <div className="register_container">
            <form>
              <h5>Add Amount</h5>
              {/* <input type="number" name="cname" value={this.state.cname} onChange={this.handleChange1} /> */}
              <Form.Group className="mb-2 mt-3" controlId="formBasicEmail">
                <Form.Label>Enter Amount</Form.Label>
                <Form.Control
                  type="number"
                  name="cname"
                  value={this.state.cname}
                  onChange={this.handleChange1}
                />
              </Form.Group>
              <br />
              {/* <Link to="/login">
                {' '} */}
              <button
                className="innerbutton"
                type="submit"
                value="Submit"
                onClick={this.submitForm}
              >
                Ok
              </button>
              {/* </Link> */}
              <br />
            </form>
          </div>
        </div>
      </Container>
    )
  }
}
