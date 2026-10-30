# The math in SkyEar

A shorthand list of the equations the app uses, and what each one means. All of them are estimates built on simple assumptions, so treat the results as guides, not measurements. The code that uses each one is named so you can check it.

## Sound (Synth)

| What | Equation | Meaning |
|---|---|---|
| Blade-pass frequency | `f = blades x RPM / 60` | Each blade passing a point makes one pulse, so the main tone is blades times turns per second. A 2-blade prop at 6,000 RPM gives 200 Hz. Load lowers RPM by up to 12%. |
| Harmonics | tone `k` is at `k x f`, with amplitude `rolloff^(k-1)` | A pulse train is not a pure sine. It has overtones at whole-number multiples of `f`, each quieter than the last. |
| Beating | `\|f1 - f2\|` | Two motors at slightly different speeds make a slow wobble at the difference of their frequencies. |
| Distance, level | `level = min(1, 10 / r)` | Sound level falls with distance (about 6 dB each time the distance doubles). |
| Distance, brightness | `cutoff = 14000 / (1 + r / 20)` Hz | Air absorbs high pitches more than low ones, so far sounds are duller. |
| Doppler shift | `f' = f x c / (c + v_r)`, with `v_r = V x x / r` | A source moving toward you sounds higher, away sounds lower. `V` is the speed (18 m/s in the fly-by), `x` the distance along the flight path from the closest point, `r` the distance to you and `c` the speed of sound. In the code the shift is `1200 x log2(c / (c + v_r))` cents. |

## Speed of sound and delay (Field tab)

| What | Equation | Meaning |
|---|---|---|
| Speed of sound | `c = 331.3 x sqrt(1 + T / 273.15)` m/s, `T` in degrees C | Sound moves faster in warmer air: 340 m/s at 15 C, 325 m/s at -10 C. When weather is off, 343 m/s (20 C) is used. Humidity and wind are ignored. |
| Sound delay | `tau = d / c` | The sound you hear now left the aircraft `tau` seconds ago. At 1 km that is about 3 seconds. |
| Straight-line distance | `d = sqrt(ground^2 + height^2)` | The slant range, using the height of the aircraft above your phone. |
| Where the sound came from | `position_then = position_now - v x tau` | The aircraft moved `v x tau` meters while the sound traveled. `d` depends on where it was, so the app repeats the calculation until `tau` settles (function `rdSolve`). |
| Position from an old report | `east = r sin(b) + v t sin(track)`, `north = r cos(b) + v t cos(track)` | Dead reckoning: start at the reported distance `r` and bearing `b`, then move along the track at ground speed `v` for the report's age `t` (capped at 45 s). Knots to m/s: multiply by 0.51444. |
| Angle above the horizon | `elevation = atan2(height, ground distance)` | How high to tilt the phone. |
| Bearing | `bearing = atan2(east, north)` | Compass direction, 0 degrees north, clockwise. |

## Position and pointing

| What | Equation | Meaning |
|---|---|---|
| Small distances on the ground | `x = (lon2 - lon1) x cos(mean lat)`, `y = lat2 - lat1`, `distance = 6,371,000 x sqrt(x^2 + y^2)` (angles in radians) | Flat-map approximation. It is accurate to a small fraction of a percent over the few hundred meters Remote ID covers. |
| Back of the phone | direction `(-R[2], -R[5], -R[8])` | `R` is the rotation matrix from the phone's sensor. The result is the east, north, up direction the camera side points. |
| Compass bearing | `atan2(east, north) + declination` | Declination corrects magnetic north to true north for your location. |
| Tilt | `elevation = asin(up)` | 0 degrees is level, 90 degrees is straight up. |
| Turn needed | `((target - heading + 540) mod 360) - 180` | The shortest turn, from -180 (left) to +180 (right). |

## Sound analysis (Listen, Library)

| What | Equation | Meaning |
|---|---|---|
| Frequency step | `sample rate / 8192` | The FFT splits sound into frequency bins. At 48 kHz each bin is about 5.9 Hz wide; the app refines the peak between bins with parabolic interpolation. |
| Harmonic present | peak at least 12 dB above the noise floor | `dB = 20 log10(amplitude ratio)`. 12 dB is a factor of about 4 in amplitude. |
| Pitch difference | `cents = 1200 x log2(f1 / f2)` | 100 cents is one semitone; 1,200 cents is an octave. |
| Preset match score | `cents / 150 + \|slope difference\| / 6 + 0.1 x \|harmonic count difference\|` | Lower is closer. Slope is how fast the harmonics fade, in dB per harmonic. |

## Quiz scoring

| What | Equation | Meaning |
|---|---|---|
| Sensitivity | `d' = z(hit rate) - z(false-alarm rate)` | `z` is the inverse of the normal curve. `d'` near 0 is guessing and above 2 is strong. It separates real skill from always answering "drone". The app adds 0.5 to the counts and 1 to the totals so a perfect score does not give infinity. |

## What the equations leave out

Wind, turbulence, reflections from buildings, humidity, aircraft that turn or climb, and the phone's own microphone response are not modeled. Compass error near metal can shift a bearing by many degrees.
