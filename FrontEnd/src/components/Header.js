import React, { useState } from 'react'
import '../Header.css'
import SearchIcon from '@material-ui/icons/Search'
import ShoppingCartIcon from '@material-ui/icons/ShoppingCart'
import AccountBalanceWalletIcon from '@mui/icons-material/AccountBalanceWallet'
import HomeIcon from '@material-ui/icons/Home'
import { Link, NavLink } from 'react-router-dom'
import { useStateValue } from './Stateprovider'
import {
  Navbar,
  Nav,
  Button,
  Form,
  FormControl,
  Container,
} from 'react-bootstrap'
import { useNavigate } from 'react-router-dom'
import httpService from '../services/httpService'
import authService from '../services/authService'

function Header() {
  const history = useNavigate()
  const [{ basket }] = useStateValue()
  const [text, setText] = useState('')
  let textInput = React.createRef()
  localStorage.setItem('text', text)
  let sign = authService.getCurrentUser()
  let signOut = () => {
    if (sign != null) {
      localStorage.removeItem('token')
      window.location.href = '/login'
    }
  }
  const makeSearchReq = (e) => {
    e.preventDefault()
    localStorage.setItem('searchText', textInput.current.value)
    // history('/search')
    history('/search', { state: textInput.current.value })
  }

  return (
    // <div className='header'>
    //     <Link to="/">
    //       <HomeIcon className='header_homeIcon'/>
    //     </Link>
    //     <Link to="/men">
    //       <p className='header_category'>Men</p>
    //     </Link>
    //     <Link to="/women">
    //       <p className='header_category'>Women</p>
    //     </Link>
    //     <div className='header_search'>
    //         <input  className='header_searchInput' ref={textInput} type="text" />
    //         <Link to="/search"><SearchIcon className='header_searchIcon' onClick={()=>(setText(textInput.current.value))}/></Link>
    //     </div >
    //     <div className='header_nav'>
    //         <Link to={!sign && "/login"}>
    //             <div className='header_option'>
    //              <span className='header_optionLineOne'>Hello {!sign ?'User':sign.ufname}</span>
    //              <span className='header_optionLineTwo' onClick={signOut}>{sign? 'Sign Out':'Sign In'}</span>
    //             </div>
    //         </Link>
    //         <Link to={"/order"}>
    //         <div className='header_option'>
    //             <span className='header_optionLineOne'>Returns</span>
    //             <span className='header_optionLineTwo'>& Order</span>
    //         </div>
    //         </Link>
    //         <Link to="/wallet">
    //             <p className='header_category'>Wallet</p>
    //         </Link>
    //         <Link to="/checkout">
    //             <div className='header_optionBasket'>
    //                 <ShoppingCartIcon />
    //                 <span className='header_optionLineTwo header_basketCount'>{basket?.length}</span>
    //             </div>
    //         </Link>
    //     </div>

    // </div>
    <Navbar bg="light" expand="lg">
      <Container fluid>
        <Link to="/" className="navbar_brand">
          <Navbar.Brand> &nbsp;</Navbar.Brand>
        </Link>
        <Navbar.Toggle aria-controls="navbarScroll" />
        <Navbar.Collapse id="navbarScroll">
          <Nav
            className="me-auto my-2 my-lg-0"
            style={{
              maxHeight: '100px',
              alignItems: 'center',
              color: '#6e1230',
            }}
            navbarScroll
          >
            <NavLink
              to={'/'}
              className={
                window.location.pathname === '/'
                  ? 'headerLink headerLinkActive'
                  : 'headerLink'
              }
            >
              Home
            </NavLink>
            <NavLink
              to={'/raw'}
              className={
                window.location.pathname === '/raw'
                  ? 'headerLink headerLinkActive'
                  : 'headerLink'
              }
            >
              Raw
            </NavLink>
            <NavLink
              to={'/stitched'}
              className={
                window.location.pathname === '/stitched'
                  ? 'headerLink headerLinkActive'
                  : 'headerLink'
              }
            >
              Stitched
            </NavLink>
            <NavLink
              to={'/about-us'}
              className={
                window.location.pathname === '/about-us'
                  ? 'headerLink headerLinkActive'
                  : 'headerLink'
              }
            >
              About Us
            </NavLink>
          </Nav>
          {/* <Form className="d-flex" onSubmit={() => (setText(textInput.current.value))}> */}
          <Form className="d-flex" onSubmit={(e) => makeSearchReq(e)}>
            <FormControl
              type="search"
              placeholder="Search"
              className="me-2"
              aria-label="Search"
              ref={textInput}
            />
            <button className="searchBtn">Search</button>
          </Form>
          <Nav.Link>
            <Link to={!sign && '/login'} className="headerLink">
              <div className="header_option">
                <span className="header_optionLineOne">
                  Hello {!sign ? 'User' : sign.u_fname}
                </span>
                <span className="header_optionLineTwo" onClick={signOut}>
                  {sign ? 'Sign Out' : 'Sign In'}
                </span>
              </div>
            </Link>
          </Nav.Link>
          <Nav.Link>
            <Link to={'/order'} className="headerLink">
              <div className="header_option">
                <span className="header_optionLineOne">Returns</span>
                <span className="header_optionLineTwo">& Order</span>
              </div>
            </Link>
          </Nav.Link>
          <Nav.Link>
            <Link to="/wallet">
              {/* <p className='header_category'>Wallet</p> */}
              <AccountBalanceWalletIcon
                fontSize="large"
                style={{ color: '#6e1230' }}
              />
            </Link>
          </Nav.Link>
          <Nav.Link>
            <Link to="/checkout" className="headerLink">
              <div className="header_optionBasket">
                <ShoppingCartIcon
                  fontSize="large"
                  style={{ color: '#6e1230' }}
                />
                <span
                  className="header_optionLineTwo header_basketCount"
                  style={{ color: '#6e1230' }}
                >
                  {basket?.length}
                </span>
              </div>
            </Link>
          </Nav.Link>
        </Navbar.Collapse>
      </Container>
    </Navbar>
  )
}

export default Header
