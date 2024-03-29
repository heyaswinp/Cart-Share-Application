import React, { Component } from "react";
import { Col, Form } from "react-bootstrap";
import Button from "react-bootstrap/Button";
import axios from "axios";
import { properties } from "../../properties";
const backendurl = properties.backendhost;

class CreatePool extends Component {
  constructor(props) {
    super(props);
    this.state = {
      leaderId: "",
      poolId: "",
      name: "",
      description: "",
      neighborhoodName: "",
      zip: "",
      errors: "",
      text: null,
      formErrors: {},
    };
    this.handleChange = this.handleChange.bind(this);
    this.handleSubmit = this.handleSubmit.bind(this);
  }

  componentDidMount() {
    this.setState({
      leaderId: localStorage.getItem("userId"),
    });
  }

  handleChange(e) {
    this.setState({
      [e.target.name]: e.target.value,
    });
  }

  validate = () => {
    var letters = /^[0-9a-zA-Z]+$/;

    var zipExp = /^\d{5}(-\d{4})?$/;

    let poolIdError = "";
    let poolNameError = "";
    let zipCodeError = "";

    if (!this.state.poolId) {
      poolIdError = "Please enter Pool Id";
    } else if (!this.state.poolId.match(letters)) {
      poolIdError = "Please input alphanumeric characters only";
    }

    if (!this.state.name) {
      poolNameError = "Please enter Pool Name";
    }
    if (!this.state.zip) {
      zipCodeError = "Please enter Zipcode";
    } else if (!this.state.zip.match(zipExp)) {
      zipCodeError =
        "The US zip code must contain 5 digits. Allowed formats are 12345 or 12345-1234";
    }

    if (poolIdError || poolNameError || zipCodeError) {
      this.setState((prevState) => ({
        formErrors: {
          // object that we want to update
          ...prevState.formErrors, // keep all other key-value pairs
          poolIdError: poolIdError, // update the value of specific key
          poolNameError: poolNameError,
          zipCodeError: zipCodeError,
        },
      }));
      return false;
    }
    return true;
  };

  handleSubmit = (e) => {
    //prevent page from refresh
    e.preventDefault();

    this.setState({
      text: "",
      errors: "",
    });

    const isValid = this.validate();
    if (isValid) {
      let data = {};
      console.log("this.state-->", this.state);
      data.leaderId = parseInt(this.state.leaderId);
      data.poolId = this.state.poolId;
      data.name = this.state.name;
      data.description = this.state.description;
      data.neighborhoodName = this.state.neighborhoodName;
      data.zip = this.state.zip;
      console.log("data-->", data);

      axios
        .post(backendurl + "pool/create", data)
        .then((response) => {
          console.log(response);
          if (response.status == 200) {
            this.setState({
              text: "Pool created successfully",
              poolId: "",
              name: "",
              description: "",
              neighborhoodName: "",
              zip: "",
              errors: "",
              formErrors: {},
            });
          }
        })
        .catch((error) => {
          console.log("Error in adding new product", error, error.response);
          this.setState({
            errors: error.response.data.message,
            formErrors: {},
          });
        });
    }
  };

  render() {
    const { text, errors } = this.state;
    return (
      <div style={{ height: "75vh" }} className="container valign-wrapper">
        <div className="row">
          <div className="col s12 center-align background blue">
            <h2 className="text-center text-white font-italic font-family-sans-serif">
              Create Pool
            </h2>
          </div>
        </div>
        <div>
          <Form>
            <Form.Row>
              <Form.Group as={Col} controlId="poolId">
                <Form.Label>Pool Id</Form.Label>
                <Form.Control
                  type="text"
                  placeholder="Enter Pool Id"
                  name="poolId"
                  value={this.state.poolId}
                  onChange={this.handleChange}
                  required
                />
                {this.state.formErrors.poolIdError ? (
                  <div style={{ fontSize: 12, color: "red" }}>
                    {this.state.formErrors.poolIdError}
                  </div>
                ) : null}
              </Form.Group>
            </Form.Row>

            <Form.Row>
              <Form.Group as={Col} controlId="name">
                <Form.Label>Name</Form.Label>
                <Form.Control
                  type="text"
                  placeholder="Enter Pool Name"
                  name="name"
                  value={this.state.name}
                  onChange={this.handleChange}
                  required
                />
                {this.state.formErrors.poolNameError ? (
                  <div style={{ fontSize: 12, color: "red" }}>
                    {this.state.formErrors.poolNameError}
                  </div>
                ) : null}
              </Form.Group>
            </Form.Row>

            <Form.Row>
              <Form.Group as={Col} controlId="description">
                <Form.Label>Description</Form.Label>
                <Form.Control
                  as="textarea"
                  placeholder="Enter Pool Description"
                  name="description"
                  value={this.state.description}
                  onChange={this.handleChange}
                  required
                />
              </Form.Group>
            </Form.Row>

            <Form.Row>
              <Form.Group as={Col} controlId="neighborhoodName">
                <Form.Label>Neighborhood Name</Form.Label>
                <Form.Control
                  type="text"
                  placeholder="Enter Neighborhood Name"
                  name="neighborhoodName"
                  value={this.state.neighborhoodName}
                  onChange={this.handleChange}
                  required
                />
              </Form.Group>
            </Form.Row>

            <Form.Row>
              <Form.Group as={Col} controlId="zip">
                <Form.Label>Zip</Form.Label>
                <Form.Control
                  type="text"
                  placeholder="Enter Zip"
                  name="zip"
                  value={this.state.zip}
                  onChange={this.handleChange}
                  required
                />
                {this.state.formErrors.zipCodeError ? (
                  <div style={{ fontSize: 12, color: "red" }}>
                    {this.state.formErrors.zipCodeError}
                  </div>
                ) : null}
              </Form.Group>
            </Form.Row>

            <Button
              className="btn btn-primary"
              type="submit"
              onClick={this.handleSubmit}
            >
              Submit
            </Button>
            <br />
            <p className="text-danger"> {errors}</p>
            <p className="text-success"> {text}</p>
            <br />
          </Form>
        </div>
      </div>
    );
  }
}

export default CreatePool;
