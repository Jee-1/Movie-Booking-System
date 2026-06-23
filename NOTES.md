# Movie Booking — Revision Notes

Simple notes to revise the project. Read top to bottom.

---

## 1. How the app is layered

A request travels through 4 layers, top to bottom:

```
Client (Postman)
      |  sends JSON
      v
Controller   -> handles the web part (URLs, JSON in/out)
      v
Service      -> the brain (rules, validation, transactions)
      v
Repository   -> talks to the database (no SQL written by hand)
      v
Entity       -> a Java class that maps to a DB table
      v
MySQL
```

**Golden rule:** Controller -> Service -> Repository. A controller never calls a repository directly.

**Helpers:**
- **DTO** = the JSON shape coming in/going out (kept separate from the entity).
- **Enum** = a fixed list of values (USER/ADMIN, REGULAR/PREMIUM), saved as text.

---

## 2. The 4 kinds of classes

| Class | Job | Example |
|-------|-----|---------|
| Entity | one class = one table | `Movie`, `Show` |
| Repository | save/find/delete rows | `MovieRepository` |
| Service | business logic | `MovieService` |
| Controller | the API endpoints | `MovieController` |
| DTO | the request body shape | `MovieRequest` |

---

## 3. Annotations — what each one does

### Entity annotations (class -> table)

| Annotation | Plain meaning |
|------------|---------------|
| `@Entity` | "this class is a table" |
| `@Table(name="movies")` | sets the table name |
| `@Id` | this field is the primary key |
| `@GeneratedValue(strategy=IDENTITY)` | DB auto-creates the id (1,2,3...) |
| `@Column(unique=true, nullable=false)` | column rules (unique / not null) |
| `@Enumerated(EnumType.STRING)` | save enum as text ("PREMIUM") not a number |
| `@ManyToOne(fetch=LAZY)` | many children -> one parent; load parent only when needed |
| `@JoinColumn(name="theater_id")` | the foreign key column name |
| `@UniqueConstraint(columnNames={...})` | no duplicate combo (e.g. same seat + same show) |

### Lombok (writes boring code for you)

| Annotation | Makes |
|------------|-------|
| `@Getter` / `@Setter` | getters / setters |
| `@NoArgsConstructor` | empty constructor (JPA & JSON need it) |
| `@AllArgsConstructor` | constructor with all fields |
| `@Data` | all of the above — **avoid on entities** (breaks with relationships) |

### Spring "this is a bean" tags

| Annotation | Meaning |
|------------|---------|
| `@Service` | a business-logic class |
| `@RestController` | a controller that returns JSON |
| `@Configuration` | a class that sets up beans |
| `@Bean` | a method that creates a bean |
| (repository) | no tag needed — extending `JpaRepository` is enough |

### Controller / web tags

| Annotation | Meaning |
|------------|---------|
| `@RequestMapping("/api/movies")` | base URL for the controller |
| `@PostMapping` | POST = create |
| `@GetMapping` | GET = read |
| `@PutMapping("/{id}")` | PUT = update |
| `@DeleteMapping("/{id}")` | DELETE = delete |
| `@RequestBody` | JSON body -> Java object |
| `@PathVariable` | value from the URL path: `/movies/5` -> 5 |
| `@RequestParam` | value from the query: `?genre=Action` |

Remember: **path = which item** (`/movies/5`), **query param = filter** (`?genre=Action`).

### Transactions

| Annotation | Meaning |
|------------|---------|
| `@Transactional` | all-or-nothing. If anything fails, undo everything. |

Used in `ShowService.create` so the show AND its seat rows save together, or none do.

---

## 4. Dependency Injection (how classes get their helpers)

**Best way — constructor injection:**
```java
private final MovieRepository repo;
public MovieService(MovieRepository repo) {   // no @Autowired needed
    this.repo = repo;
}
```
Why: fields can be `final`, dependencies are clear, easy to test.

**Old way — field injection (avoid):**
```java
@Autowired
private MovieRepository repo;   // can't be final, hides dependencies
```

---

## 5. The data model (the big idea)

```
Theater -> Screen -> Seat        (static: the building & chairs)
Movie                            (the film)
Show = Movie + Screen + time     (one screening)
SeatBooking = one seat's status FOR one show  (AVAILABLE/LOCKED/BOOKED)
Booking -> BookingSeat + Payment (a customer's purchase)
```

**Key insight:** a `Seat` (chair) has no status. Whether it's free depends on the **show**.
So per-show status lives in **SeatBooking**. That's why seat A1 can be BOOKED at 6pm
but FREE at 9pm — two different SeatBooking rows, same chair.

When a Show is created, the app auto-creates one AVAILABLE SeatBooking per seat.

---

## 6. Things to remember (interview-ready)

1. **DTO, not entity, in APIs** — keeps the public shape separate from the DB.
2. **`@Transactional`** = all-or-nothing (show + seats together).
3. **`save()`** does INSERT if id is null, UPDATE if id exists.
4. **FK-by-id**: DTO carries `theaterId` (a number); the service looks up the real Theater.
5. **LAZY loading** = load the parent only when you touch it (avoids extra queries).
6. **`BigDecimal` for money**, never `double` (rounding bugs).
7. **Enum as STRING**, not a number (readable + safe).
8. **ID gaps are normal** — ids identify a row, they are not a position. Never reuse them.

---

## 7. Progress

- [x] Phase 1: Project + DB setup
- [x] Phase 2: Package structure
- [x] Phase 3: Entities (10) + enums
- [x] Phase 4: Repositories
- [x] Phase 5: Admin CRUD (Movie, Theater, Screen, Seat, Show + auto seat-map)
- [ ] Phase 6: Global exception handling (clean 404s)
- [ ] Phase 7: Public browse APIs (seat map for a show)
- [ ] Phase 8: Spring Security + JWT (login, roles)
- [ ] Phase 9: Seat locking + booking  <-- the centerpiece
- [ ] Phase 10: Payment + polish (validation, pagination, tests)
