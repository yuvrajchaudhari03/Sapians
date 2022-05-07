import React from 'react'
import '../register.css'
import { Link } from 'react-router-dom'
import HomeIcon from '@material-ui/icons/Home'
import { Container } from 'react-bootstrap'
import Logo from '../assets/img/Logo.png'
import authService from '../services/authService'
import httpService from '../services/httpService'

export default class ViewOrderbyuid extends React.Component {
  constructor(props) {
    super(props)
    this.state = {
      sign: authService.getCurrentUser(),
      walletBalance: null,
    }
  }
  submitForm = () => {
    window.location.href = '/'
  }
  componentDidMount = async () => {
    const url = process.env.REACT_APP_BASE_URL + '/user/my_profile'
    let user = await httpService.get(url)
    this.setState({ walletBalance: user.data.wallet })
  }
  render() {
    const to1 = this.state.sign
    return (
      <Container className="login">
        <div style={{ textAlign: 'center' }}>
          <Link to="/">
            <img className="login_img" src={Logo} alt="logo" />
          </Link>
        </div>
        {to1 != null ? (
          <div className="register">
            <div className="register_container">
              <h2>User Wallet</h2>
              <br />
              <form>
                <h5>Amount in wallet : {this.state.walletBalance}</h5>
                <br />
                <Link to="/addmoney">
                  <button
                    className="innerbutton"
                    type="submit"
                    value="Submit" /* onClick={this.submitForm}*/
                  >
                    Add Amount
                  </button>
                </Link>
                <br />
              </form>
            </div>
          </div>
        ) : (
          <div style={{ textAlign: 'center', color: 'black' }}>
            <h2>Please Login</h2>
          </div>
        )}
      </Container>
    )
  }
}
