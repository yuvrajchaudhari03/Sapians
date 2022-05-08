import React from 'react'
import '../VendorHome.css'
import { Table } from 'react-bootstrap'
import Loader from './Loader'
import authService from '../services/authService'
import httpService from '../services/httpService'
import { toast } from 'react-toastify'

export default class VendorHome extends React.Component {
  constructor(props) {
    super(props)
    this.state = {
      to: [],
      loading: false,
    }
  }
  componentDidMount = async () => {
    this.setState({ loading: true })
    let sign = authService.getCurrentUser()
    const url = process.env.REACT_APP_BASE_URL + '/product/byVendor/IS'
    let products = await httpService.get(url)
    this.setState({ to: products.data, loading: false })
  }
  render() {
    return this.state.loading ? (
      <Loader />
    ) : (
      <div className="vhome">
        {this.state.to.length !== 0 ? (
          <div className="vhome_container">
            <div className="vhome_row">
              <Table striped bordered hover style={{ textAlign: 'center' }}>
                <thead>
                  <tr style={{ backgroundColor: '#6e1230', color: 'white' }}>
                    <th>Product ID</th>
                    <th>Product Title</th>
                    <th>Product Describe</th>
                    <th>Product Size</th>
                    <th>Product Brand</th>
                    <th>Product Price</th>
                    <th>Product Quantity</th>
                    <th>Product Rating</th>
                  </tr>
                </thead>
                <tbody>
                  {this.state.to.map((o) => {
                    return (
                      <tr>
                        <td>{o.p_id}</td>
                        <td>{o.pname}</td>
                        <td>{o.pdesc}</td>
                        <td>{o.psize}</td>
                        <td>{o.pbrand}</td>
                        <td>{o.pprice}</td>
                        <td>{o.pqty}</td>
                        <td>{o.prating}</td>
                      </tr>
                    )
                  })}
                </tbody>
              </Table>
            </div>
            <div className="">
              Total Number Of Products:
              <br />
              {this.state.to.length}
            </div>
          </div>
        ) : (
          <div style={{ textAlign: 'center', color: 'black' }}>
            <h2>No Data</h2>
          </div>
        )}
      </div>
    )
  }
}
