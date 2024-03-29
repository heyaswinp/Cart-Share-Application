import axios from "axios";
import { GET_ERRORS } from "./types";
import { SET_PROFILE_DATA } from "./types";
import { UPDATE_PROFILE_DATA } from "./types";
import { properties } from "../../properties";
const backendurl = properties.backendhost;

//Get User Profile
export const getUserProfile = () => (dispatch) => {
  let email = localStorage.getItem("email");

  console.log("Printing email_id " + email);
  axios
    .get(backendurl + "/user?email=" + email)
    .then((response) => {
      dispatch({
        type: SET_PROFILE_DATA,
        payload: response.data,
      });
      localStorage.setItem("verified", response.data.verified);
    })
    .catch((error) => {
      dispatch({
        type: GET_ERRORS,
        payload: error.response.data,
      });
    });
};

//Update User Profile

// export const updateUserProfile = data => dispatch => {
//   axios
//     .post("http://" + rooturl + ":3500/api/profile/updprofile", data)
//     .then(response => {
//       console.log("Status Code : ", response.status);
//       console.log("update profile response" + response);
//       if (response.status === 200) {
//         console.log("Update Profile Request successful");
//         dispatch({
//           type: UPDATE_PROFILE_DATA,
//           payload: response.data
//         });
//       }
//     })
//     .catch(error => {
//       dispatch({
//         type: GET_ERRORS,
//         payload: error.response.data
//       });
//     });
// };
