import React from 'react'
import '../compheader.css'
import { Form } from 'react-bootstrap'
import { Table } from 'react-bootstrap'
import httpService from '../services/httpService'

export default class ViewVendor extends React.Component {
  constructor(props) {
    super(props)
    this.state = {
      to: [],
    }
  }
  componentDidMount = async () => {
    const url = process.env.REACT_APP_BASE_URL + '/user/getallvendor'
    let data = await httpService.get(url)
    this.setState({ to: data.data })
  }
  onChangeAproveStatus = async (e, o) => {
    e.preventDefault()
    let allVendors = [...this.state.to]
    allVendors
      .filter((v) => v.v_id === o.v_id)
      .map(async (v) => {
        console.log(v.v_status)

        await fetch(
          process.env.REACT_APP_BASE_URL +
            '/vendor/approve/' +
            o.v_id +
            '/' +
            !v.v_status,
          {
            method: 'PATCH', // or 'PUT'
            headers: {
              'Content-Type': 'application/json',
            },
          },
        )
          .then((response) => response.json())
          .then((data) => {
            v.v_status = !v.v_status
            this.setState({ to: allVendors })
          })
          .catch((error) => {
            console.error('Error:', error)
          })
      })
  }
  render() {
    const to1 = this.state.to.length
    return (
      <div>
        {to1 != 0 ? (
          <div className="vhome">
            <div className="vhome_container">
              <div className="mt-3">
                <Table striped bordered hover style={{ textAlign: 'center' }}>
                  <thead>
                    <tr style={{ backgroundColor: '#6e1230', color: 'white' }}>
                      <th>Vendor ID</th>
                      <th>Vendor FirstName</th>
                      <th>Vendor ContactNumber</th>
                      <th>Vendor Email</th>
                      <th>Vendor Address</th>
                      <th>Vendor Wallet</th>
                      <th>Vendor Approve</th>
                    </tr>
                  </thead>
                  <tbody>
                    {this.state.to.map((o) => {
                      return (
                        <tr>
                          <td>{o.u_id}</td>
                          <td>{o.u_fname}</td>
                          <td>{o.u_phone}</td>
                          <td>{o.u_email}</td>
                          <td>{o.u_address}</td>
                          <td>{o.u_wallet}</td>
                          <td>
                            <Form.Check
                              type="switch"
                              id="custom-switch5"
                              checked={
                                o.v_status === 'true' || o.v_status === true
                                  ? true
                                  : false
                              }
                              onChange={(e) => this.onChangeAproveStatus(e, o)}
                              className="mt-2"
                            />
                          </td>
                        </tr>
                      )
                    })}
                  </tbody>
                </Table>
              </div>
              <div className="vhome_row">
                Total Number Of Vendors:
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
