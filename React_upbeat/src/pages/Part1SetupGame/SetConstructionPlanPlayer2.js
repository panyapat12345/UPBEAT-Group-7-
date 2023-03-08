import React from 'react'
import Link from 'next/link'


function SetConstructionPlanPlayer2() {
  return (
    <div>
         <li>
        <Link href="/">Home</Link>
      </li>
      <h2>SetConstructionPlanPlayer2</h2>
      <li>
       <textarea
          rows={30}
          cols={100}
        ></textarea>
      </li>
        <li>
        <button>
        <Link href="/Part1SetupGame/SetConstructionPlanPlayer1">Back</Link>
        </button>
      </li>
      <li>
        <button>
        <Link href="/Part2Gameplay/Territory">Start</Link>
        </button>
      </li>
    </div>
  )
}

export default SetConstructionPlanPlayer2