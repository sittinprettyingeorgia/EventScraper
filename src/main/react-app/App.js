import React, { useEffect } from 'react';
import { LIST } from './constants/pageConstants';

function App() {
  const [data, setData] = useState();

  useEffect(() => {
    const getData = async() => {

      let pages = {
        typeOfData: "event",
        pages: LIST
      }

      fetch('/crawl', {
        method: 'post',
        headers: {
          'content-type':'application/json'
        },
        body: JSON.stringify(pages)
      })
          .then(response => response.json())
          .then(json => console.log(json));
    };
  });
  return (
    <div className="App">

    </div>
  );
}

export default App;
