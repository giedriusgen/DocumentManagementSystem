import React from "react";
import { withRouter } from "react-router-dom";
import { Form } from "react-bootstrap";
import { Button } from "react-bootstrap";
import { Formik } from "formik";
import { Modal } from "react-bootstrap";
import * as yup from "yup";
import axios from "axios";
import PasswordChangeComponent from "../../../../PasswordChange/PasswodChange";
import serverUrl from "../../../../URL/ServerUrl";
import AssignGroupsContainer from "./AssignGroupsPage/AssignGroupsContainer";

const schema = yup.object({
  firstName: yup
    .string()
    .trim()
    .min(1, "Must be 1 characters or more")
    .max(30, "Must be 30 characters or less")
    .required("Please enter a first name")
    .matches(
      /^[A-Za-z\s-]+$/,
      "Only uppercase, lowercase letters and '-', space symbols are allowed"
    ),
  lastName: yup
    .string()
    .trim()
    .min(1, "Must be 1 characters or more")
    .max(30, "Must be 30 characters or less")
    .required("Please enter a last name")
    .matches(
      /^[A-Za-z\s-]+$/,
      "Only uppercase, lowercase letters and '-', space symbols are allowed"
    ),
  comment: yup
    .string()
    .trim()
    .max(50, "Must be 50 characters or less")
});

const handleSubmit = values => {
  console.log("handle submit method");
};

class UserReviewContainer extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      showPasswordChangeModal: false,
      showAssignToGroupsModal: false,
      users: [],
      inputUsername: "",
      firstName: "",
      lastName: "",
      comment: "",
      userGroups: [],
      user: []
    };
  }

  getUser = () => {
    axios
      .get(serverUrl + "api/user/" + this.props.match.params.username)
      .then(response => {
        this.setState({
          user: response.data,
          userGroups: response.data.userGroups,
          firstName: response.data.firstName,
          lastName: response.data.lastName,
          comment: response.data.comment
        });
      })
      .catch(error => {
        console.log(error);
      });
  };

  componentDidMount() {
    this.getUser();
  }

  handleClosePasswordChangeModal = () => {
    this.setState({ showPasswordChangeModal: false });
  };

  handleShowPasswordChangeModal = () => {
    this.setState({ showPasswordChangeModal: true });
  };

  handleShowAssignToGroupsModal = () => {
    this.setState({ showAssignToGroupsModal: true });
  };

  handleCloseAssignToGroupsModal = () => {
    this.setState({ showAssignToGroupsModal: false });
  };

  handleCloseAssignToGroupsModalAndUpdate = () => {
    this.setState({ showAssignToGroupsModal: false });
    this.getUser();
  };

  handleFirstNameChange = event => {
    this.setState({ firstName: event.target.value });
  };

  handleLastNameChange = event => {
    this.setState({ lastName: event.target.value });
  };

  handleCommentChange = event => {
    this.setState({ comment: event.target.value });
  };

  onCancelClick = event => {
    event.preventDefault();
    this.props.history.push("/adminhomepage-users");
  };

  updateUser = event => {
    event.preventDefault();
    axios
      .put(
        serverUrl + "api/user/update-user-info/" + this.state.user.username,
        {
          comment: this.state.comment,
          firstName: this.state.firstName,
          lastName: this.state.lastName,
          password: "Stingaaa1",
          username: "String"
        }
      )
      .then(() => {
        this.props.history.push("/adminhomepage-users");
      })
      .catch(error => {
        console.log(error);
      });
  };

  render() {
    return (
      <div className="container">
        <Formik
          validationSchema={schema}
          onSubmit={handleSubmit}
          initialValues={{
            firstName: "first name",
            lastName: "last name",
            comment: "comment"
          }}
        >
          {({ handleChange, values, isValid, errors }) => (
            <div className="UserReviewForm">
              <Form noValidate>
                <div className="row">
                  <div className="row d-flex justify-content-center  col-12  m-0">
                    <div className="col-5 d-flex flex-column col-6">
                      <div>
                        <Form.Group>
                          <Form.Control
                            type="username"
                            placeholder={this.state.user.username}
                            size="lg"
                            disabled
                          ></Form.Control>
                        </Form.Group>

                        <Form.Group>
                          <label htmlFor="firstName">First Name</label>
                          <Form.Control
                            type="firstname"
                            placeholder="First name"
                            defaultValue={this.state.user.firstName}
                            onChange={this.handleFirstNameChange}
                            onKeyUp={handleChange}
                            name="firstName"
                            id="firstName"
                            className="UserReviewForm"
                            size="lg"
                            isInvalid={!!errors.firstName}
                          />
                          <Form.Control.Feedback
                            className="FeedBack"
                            type="invalid"
                          >
                            {errors.firstName}
                          </Form.Control.Feedback>
                        </Form.Group>
                        <Form.Group>
                          <label htmlFor="lastName">Last Name</label>
                          <Form.Control
                            type="lastname"
                            placeholder="Last name"
                            defaultValue={this.state.user.lastName}
                            onChange={this.handleLastNameChange}
                            onKeyUp={handleChange}
                            name="lastName"
                            id="lastName"
                            className="UserReviewForm"
                            size="lg"
                            isInvalid={!!errors.lastName}
                          />
                          <Form.Control.Feedback
                            className="FeedBack"
                            type="invalid"
                          >
                            {errors.lastName}
                          </Form.Control.Feedback>
                        </Form.Group>
                        <Form.Group>
                          <label htmlFor="comment">Comment</label>
                          <Form.Control
                            as="textarea"
                            rows="2"
                            className="UserReviewForm"
                            size="lg"
                            name="comment"
                            onChange={this.handleCommentChange}
                            onKeyUp={handleChange}
                            type="comment"
                            id="comment"
                            defaultValue={this.state.user.comment}
                            placeholder="Comment"
                            isInvalid={!!errors.comment}
                          />
                          <Form.Control.Feedback
                            className="FeedBack"
                            type="invalid"
                          >
                            {errors.comment}
                          </Form.Control.Feedback>
                        </Form.Group>
                        <button
                          type="button"
                          className="col-12 mb-1 btn btn-info btn-sm"
                          onClick={this.handleShowAssignToGroupsModal}
                        >
                          Assign to groups
                        </button>
                        <Modal
                          show={this.state.showAssignToGroupsModal}
                          onHide={this.handleCloseAssignToGroupsModalAndUpdate}
                        >
                          <Modal.Header closeButton>
                            <Modal.Title>Assign to groups</Modal.Title>
                          </Modal.Header>
                          <Modal.Body>
                            <AssignGroupsContainer
                              userGroups={this.state.user.userGroups}
                              username={this.state.user.username}
                              onHide={
                                this.handleCloseAssignToGroupsModalAndUpdate
                              }
                            />
                          </Modal.Body>
                        </Modal>
                        <button
                          type="button"
                          className="col-12 btn btn-info btn-sm"
                          onClick={this.handleShowPasswordChangeModal}
                        >
                          Change password
                        </button>
                        <Modal
                          show={this.state.showPasswordChangeModal}
                          onHide={this.handleClosePasswordChangeModal}
                        >
                          <Modal.Header closeButton>
                            <Modal.Title>Change password</Modal.Title>
                          </Modal.Header>
                          <Modal.Body>
                            <PasswordChangeComponent
                              onCloseModal={this.handleClosePasswordChangeModal}
                              username={this.state.user.username}
                            />
                          </Modal.Body>
                        </Modal>
                      </div>
                    </div>
                    <div className="col-6  m-0">
                      <div className="card">
                        <div className="card-body">
                          <h5 className="card-title">User groups</h5>
                          <div className="card-text scroll">
                            <ul className="list-group mb-2">
                              {this.state.userGroups.map((group, index) => {
                                return (
                                  <div key={index}>
                                    <li className="list-group-item">{group}</li>
                                  </div>
                                );
                              })}
                            </ul>
                          </div>
                        </div>
                      </div>
                    </div>
                  </div>
                </div>
                <div className="d-flex justify-content-center col-12 mt-2">
                  <Button
                    disabled={!values.firstName || !values.lastName || !isValid}
                    onClick={this.updateUser}
                    variant="primary"
                    className="SubmitButton mr-2 col-4"
                    type="button"
                  >
                    Submit
                  </Button>
                  <Button
                    onClick={this.onCancelClick}
                    variant="secondary"
                    className="col-4"
                  >
                    Cancel
                  </Button>
                </div>
              </Form>
            </div>
          )}
        </Formik>
      </div>
    );
  }
}

export default withRouter(UserReviewContainer);
