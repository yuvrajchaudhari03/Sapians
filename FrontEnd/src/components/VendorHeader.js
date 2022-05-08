import React, { useEffect } from 'react'
import '../VendorHeader.css'
import HomeIcon from '@material-ui/icons/Home'
import { Link, NavLink } from 'react-router-dom'
import {
  Navbar,
  Nav,
  Button,
  Form,
  FormControl,
  Container,
} from 'react-bootstrap'
import AccountBalanceWalletIcon from '@mui/icons-material/AccountBalanceWallet'
import authService from '../services/authService'
import httpService from '../services/httpService'
import { toast } from 'react-toastify'

function VendorHeader(props) {
  let sign = authService.getCurrentUser()
  let signOut = () => {
    if (sign != null) {
      authService.logout()
      window.location.href = '/login'
    }
  }

  useEffect(() => {
    let sign = authService.getCurrentUser()
    if (!sign.authorities.includes('VENDOR')) {
      toast("You'r not authorized to visit this page. You'r not a vendor.")
      setTimeout(() => {
        window.location.href = '/'
      }, 5000)
    }
  }, [])

  return (
    <Navbar bg="light" expand="lg">
      <Container fluid>
        <Link to="/vendor" className="navbar_brand">
          <Navbar.Brand> &nbsp;</Navbar.Brand>
        </Link>
        <Navbar.Toggle aria-controls="navbarScroll" />
        <Navbar.Collapse id="navbarScroll">
          <Nav
            className="me-auto my-2 my-lg-0"
            style={{ maxHeight: '100px' }}
            navbarScroll
          >
            {/* <NavLink to={"/addcategory"} className="headerLink" >Vendor</NavLink> */}
            <NavLink
              to={'/vendor'}
              className={
                window.location.pathname === '/vendor'
                  ? 'headerLink headerLinkActive'
                  : 'headerLink'
              }
            >
              View Products
            </NavLink>
            <NavLink
              to={'/viewproductoutofstock'}
              className={
                window.location.pathname === '/viewproductoutofstock'
                  ? 'headerLink headerLinkActive'
                  : 'headerLink'
              }
            >
              Product Out Of Stock
            </NavLink>
            <NavLink
              to={'/addproduct'}
              className={
                window.location.pathname === '/addproduct'
                  ? 'headerLink headerLinkActive'
                  : 'headerLink'
              }
            >
              Add Products
            </NavLink>
          </Nav>
          <Nav.Link>
            <Link to={'/vwallet'} className="headerLink">
              <AccountBalanceWalletIcon
                fontSize="large"
                style={{ color: '#6e1230' }}
              />
            </Link>
          </Nav.Link>
          <Nav.Link>
            <Link to={!sign && '/vendorlogin'} className="headerLink">
              <div className="vheader_option">
                <span className="vheader_optionLineOne">
                  Hello {!sign ? 'Vendor' : sign.v_name}
                </span>
                <span className="vheader_optionLineTwo" onClick={signOut}>
                  {sign ? 'Sign Out' : 'Sign In'}
                </span>
              </div>
            </Link>
          </Nav.Link>
          <Nav.Link style={{ color: '#6e1230' }}>
            <Link to={'/vendor'} className="headerLink">
              Vendor
            </Link>
          </Nav.Link>
        </Navbar.Collapse>
      </Container>
    </Navbar>
  )
}
export default VendorHeader
