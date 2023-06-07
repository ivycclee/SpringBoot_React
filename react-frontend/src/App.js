import React from "react";
import Navigation from "./components/header";

// class App extends Component {
//   state = {
//     clients: []
//   };
//
//   async fetchIndex() {
//     fetch("/");
//   }
//
//   render() {
//     const test = <p>hmm</p>;
//     const form = <div>  </div>;
//
//     return (
//             <div className='container'>
//               <div className="border border-warning p-5">
//                 <h1 className="text-center pb-2">Search the database</h1>
//               </div>
//             </div>
//
//     );
//
//   }

// }//end class

function App() {
  return (
      <div className="App">
        <Navigation/>
      </div>
  )
}

export default App;
