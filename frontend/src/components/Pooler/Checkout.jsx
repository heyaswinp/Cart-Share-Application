import React, {Component} from "react";
import {Button, Card, Col, Container, Row} from "react-bootstrap";
import { properties } from "../../properties";
import axios from 'axios';
import swal from 'sweetalert';
import Spinner from "react-bootstrap/Spinner";

class Checkout extends Component {

  constructor(props) {
    super(props);
    this.state = {
      loading: false
    };
  }

  componentDidMount = async () => {

    let cart = JSON.parse(localStorage.getItem("cart"));
    //console.log("values from local storage, Cart" + cart);
    //console.log("values from local storage, StoreId" + localStorage.getItem("cart_store_id"));

    let storeResponse = await axios.get(properties.backendhost + 'store/' + localStorage.getItem("cart_store_id"));
    //console.log(storeResponse.data);
    let store = storeResponse.data;

    console.log(cart);
    console.log(store);

    if(cart && cart.length > 0) {

      let totalBasePrice = 0;
      for(let i = 0; i < cart.length; i++) {
        totalBasePrice += cart[i].amount;
      }
      //console.log(totalBasePrice);

      let tax = totalBasePrice*0.0925;
      tax = tax.toFixed(2);
      tax = Number(tax);
      //console.log(tax);

      let convenienceFee = totalBasePrice*0.005;
      convenienceFee = convenienceFee.toFixed(2);
      convenienceFee = Number(convenienceFee);
      //console.log(convenienceFee);

      let finalOrderTotal = totalBasePrice + tax + convenienceFee;
      finalOrderTotal = finalOrderTotal.toFixed(2);
      finalOrderTotal = Number(finalOrderTotal);
      //console.log(finalOrderTotal);

      let order = {};
      order.totalBasePrice = totalBasePrice;
      order.tax = tax;
      order.convenienceFee = convenienceFee;
      order.finalOrderTotal = finalOrderTotal;
      localStorage.setItem("finalOrderTotal", finalOrderTotal)
      //console.log(order);

      this.setState({
        cart: cart,
        store: store,
        order: order
      });
    }

  }

  handleDeferPickup = async () => {
    console.log('into Defer pickup click');

    try {
      let userResponse = await axios.get(properties.backendhost + 'user/?email=' + localStorage.getItem("email"));
      //console.log(userResponse.data);

      if(userResponse.data.creditScore <= -6) {
        swal({
          title: 'Should you proceed!',
          text: 'Your contribution credit is: ' + userResponse.data.creditScore + ' You are in red zone,Please start picking up some orders',
          icon: 'warning',
          buttons: true,
          dangerMode: true
        }).then((proceed) => {
          if(proceed) {
            this.deferPickUp();
          }else {
            swal('You have decided not to proced.');
          }
        });
      } else if(userResponse.data.creditScore <= -4) {
        swal({
          title: 'Should you proceed!',
          text: 'Your contribution credit is: ' + userResponse.data.creditScore + ' You are in yellow zone,Please start picking up some orders',
          icon: 'warning',
          buttons: true,
          dangerMode: true
        }).then((proceed) => {
          if(proceed) {
            this.deferPickUp();
          }else {
            swal('You have decided not to proced.');
          }
        });
      } else {
        this.deferPickUp();
      }


    }catch (e) {
      console.log(e.response);
      swal(e.response.data.message);
    }
  }

  deferPickUp = async () => {

    try {
      let userResponse = await axios.get(properties.backendhost + 'user/?email=' + localStorage.getItem("email"));
      //console.log(userResponse.data);

      this.setState({
        loading: true,
      });

      let postBody = {};
      postBody.poolerId = localStorage.getItem("userId");
      postBody.price = this.state.order.finalOrderTotal;
      postBody.poolId = userResponse.data.poolId;
      postBody.storeId = localStorage.getItem("cart_store_id");
      postBody.items = [];
      for(let i = 0; i < this.state.cart.length; i++) {
        let item = {};
        item.productId = this.state.cart[i].id;
        item.quantity = this.state.cart[i].quantity;
        item.price = this.state.cart[i].price;
        postBody.items.push(item);
      }
      //console.log(postBody);

      let orderResponse = await axios.post(properties.backendhost + 'order/defer', postBody);
      //console.log(orderResponse.data);

      swal({
        title: 'Order placed!',
        icon: 'success'
      });

      localStorage.removeItem("cart");
      localStorage.removeItem("cart_store_id");
      const { history } = this.props;
      history.push('/main/home');
    }catch (e) {
      console.log(e.response);
      swal(e.response.data.message);
      this.setState({
        loading: false,
      });
    }

  }

  handleSelfPickupSubmit = () => {
    const { history } = this.props;
    history.push('/main/selfPickup');
  }

  render () {
    const {cart, store, order, loading} = this.state;
    console.log(this.state);
    let spinner;
    if (loading) {
      spinner = <Spinner animation="border" variant="primary" />;
    }
    if(cart && store && order) {
      return (
        <div style={{ height: "75vh" }} className="container valign-wrapper">
          <div className="row">
            <div className="col s12 center-align background blue">
              <h2 className="text-center text-white font-italic font-family-sans-serif">
                Checkout - You are ordering from {store.name}
              </h2>
            </div>
          </div>
          <Container>
            <Row>
              <Col md={{ span: 10, offset: 1 }}>
                <div>
                  <table className="table table-bordered table-hover">
                    <thead className="thead">
                      <tr>
                      <th
                        className="text-center  font-weight-bold"
                        scope="col"
                      >
                        Item Name
                      </th>
                      <th
                        className="text-center  font-weight-bold"
                        scope="col"
                      >
                        Quanity
                      </th>
                      <th
                        className="text-center  font-weight-bold"
                        scope="col"
                      >
                        Price
                      </th>
                      <th
                        className="text-center  font-weight-bold"
                        scope="col"
                      >
                        Amount
                      </th>
                    </tr>
                    </thead>
                    <tbody>

                      {cart &&
                      cart.map((item, rowIndex) => {
                        return (
                          <tr key={rowIndex}>
                            <td className="text-center" scope="row">
                              {item.name}
                            </td>
                            <td className="text-center" scope="row">
                              {item.quantity}
                            </td>
                            <td className="text-center" scope="row">
                              {item.price}
                            </td>
                            <td className="text-center" scope="row">
                              {item.amount}
                            </td>
                          </tr>
                        );
                      })}

                      <tr>
                        <td className="text-center" colSpan="3">
                          <h5> Total Base Price </h5>
                        </td>

                        <td className="text-center">
                              <span className="badge badge-primary badge-pill">
                                <h5> ${order.totalBasePrice} </h5>
                              </span>
                        </td>
                      </tr>
                      <tr>
                        <td className="text-center" colSpan="3">
                          <h5> Tax </h5>
                        </td>

                        <td className="text-center">
                              <span className="badge badge-primary badge-pill">
                                <h5> ${order.tax} </h5>
                              </span>
                        </td>
                      </tr>
                      <tr>
                        <td className="text-center" colSpan="3">
                          <h5> Convenience Fee </h5>
                        </td>

                        <td className="text-center">
                              <span className="badge badge-primary badge-pill">
                                <h5> ${order.convenienceFee} </h5>
                              </span>
                        </td>
                      </tr>
                      <tr>
                        <td className="text-center" colSpan="3">
                          <h5> Final Order Total </h5>
                        </td>

                        <td className="text-center">
                              <span className="badge badge-primary badge-pill">
                                <h5> ${order.finalOrderTotal} </h5>
                              </span>
                        </td>
                      </tr>
                    </tbody>
                  </table>
                </div>
              </Col>
            </Row>
            <Row>
              <Col md={{ span: 10, offset: 1 }}>
                <Button onClick={this.handleSelfPickupSubmit}>
                  Self pickup
                </Button>
              </Col>
            </Row>
            <Row>
              <Col md={{ span: 10, offset: 1 }}>
                <Button
                onClick={this.handleDeferPickup}>
                  Defer pickup
                </Button>
                {spinner}
              </Col>
            </Row>
          </Container>
        </div>
      );
    } else {
      return (
        <div style={{ height: "75vh" }} className="container valign-wrapper">
          <div className="row">
            <div className="col s12 center-align background blue">
              <h2 className="text-center text-white font-italic font-family-sans-serif">
                Check-out
              </h2>
            </div>
          </div>
          <Container>
            <Row>
              <Col md={{ span: 10, offset: 4 }}>
                <Card style={{ width: "18rem" }}>
                  <Card.Body>
                    <Card.Title>
                      There is nothing in the cart at the moment, You can't checkout anything!
                    </Card.Title>
                    <Card.Text>Please add items to Cart!</Card.Text>
                  </Card.Body>
                </Card>
              </Col>
            </Row>
          </Container>
        </div>
      );
    }
  }
}

export default Checkout;