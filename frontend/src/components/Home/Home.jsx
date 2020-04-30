import React, { Component } from "react";
import { Redirect, withRouter } from "react-router-dom";
import PropTypes from "prop-types";
import PoolerHome from "./PoolerHome";
import AdminHome from "./AdminHome";
import decode from "jwt-decode";
import axios from "axios";
import { properties } from "../../properties";
// import firebase from 'firebase';

class Home extends Component {
  constructor(props) {
    super(props);
    this.state = {
      role: null,
      profileCompleted: null,
      responseStatus: false,
      verified: false,
    };
  }

  // logout = () => {
  //   console.log("inside onclick")
  //   var that = this
  //   firebase.auth().signOut().then(function() {
  //     console.log("Logging out")
  //     localStorage.removeItem("email")
  //     localStorage.removeItem("uid")
  //     localStorage.removeItem("firebaseui::rememberedAccounts")
  //     // that.props.history.push("/Test");
  //     window.location.href = "/"
  //     // Sign-out successful.
  //   }, function(error) {
  //     console.log("error in logging out")
  //     // An error happened.
  //   });
  // }

  async componentDidMount() {
    console.log("In Base Home ----------------");

    try {
      axios.defaults.withCredentials = true;
      const backendurl =
        properties.backendhost + "user/?email=" + localStorage.getItem("email");
      //console.log(backendurl);
      let response = await axios.get(backendurl);
      console.log(response);
      if (response.data) {
        console.log("the screen name of user is " + response.data.screenName);
        localStorage.setItem("screenName", response.data.screenName);
        localStorage.setItem("nickName", response.data.nickName);
        localStorage.setItem("userId", response.data.id);
        this.setState({
          role: response.data.role,
          profileCompleted: response.data.profileCompleted,
          verified: response.data.verified,
          responseStatus: true,
        });
      } else {
        console.log("Error while retrieving response");
      }
    } catch (error) {
      console.log(error);
    }
  }

  render() {
    var homeComponent;

    let redirectVar;

    if (this.state.responseStatus && !this.state.profileCompleted) {
      console.log(this.state.responseStatus);
      console.log(this.state.profileCompleted);
      console.log("Into redirect");
      redirectVar = <Redirect to="/main/userDetailsForm" />;
    }

    if (
      this.state.responseStatus &&
      this.state.profileCompleted &&
      this.state.verified &&
      this.state.role === "admin"
    ) {
      homeComponent = <AdminHome />;
    } else if (
      this.state.responseStatus &&
      this.state.profileCompleted &&
      this.state.verified &&
      this.state.role === "pooler"
    ) {
      homeComponent = <PoolerHome />;
    }
    // else {
    //   this.props.history.push("/");
    // }

    // return <div><button onClick={this.logout}>Logout</button></div>
    return (
      <div>
        {redirectVar}
        {homeComponent}
      </div>
    );
  }
}

export default Home;
