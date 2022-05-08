import React from 'react'
import '../compheader.css'
import { Table } from 'react-bootstrap'
import httpService from '../services/httpService'

export default class ViewCustomer extends React.Component {
  constructor(props) {
    super(props)
    this.state = {
      to: [],
    }
  }
  componentDidMount = async () => {
    const url = process.env.REACT_APP_BASE_URL + '/user/getalluser'
    let data = await httpService.get(url)
    this.setState({ to: data.data })
  }

  render() {
    const to1 = this.state.to.length
    return (
      <div>
        {to1 != 0 ? (
          <div className="vhome">
            <div className="vhome_container">
              <div className="">
                <Table striped bordered hover style={{ textAlign: 'center' }}>
                  <thead>
                    <tr style={{ backgroundColor: '#6e1230', color: 'white' }}>
                      <th>Customer ID</th>
                      <th>Customer FirstName</th>
                      <th>Customer LastName</th>
                      <th>Customer Email</th>
                      <th>Customer Address</th>
                      <th>Customer ContactNumber</th>
                      <th>Customer Wallet</th>
                    </tr>
                  </thead>
                  <tbody>
                    {this.state.to.map((o) => {
                      return (
                        <tr>
                          <td>{o.u_id}</td>
                          <td>{o.u_fname}</td>
                          <td>{o.u_lname}</td>
                          <td>{o.u_email}</td>
                          <td>{o.u_address}</td>
                          <td>{o.u_phone}</td>
                          <td>{o.wallet}</td>
                        </tr>
                      )
                    })}
                  </tbody>
                </Table>
              </div>
              <div className="vhome_row">
                Total Number Of Customer:
                <br />
                {this.state.to.length}
              </div>
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
