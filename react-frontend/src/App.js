import {Component} from "react";

class App extends Component {
  state = {
    clients: []
  };

  async componentDidMount() {
    const resp = await fetch("http://localhost:8080/petopia/all");
    const body = await resp.json();
    this.setState({clients: body});
  }

  render() {
    const {clients} = this.state;

  }

}//end class

export default App;
