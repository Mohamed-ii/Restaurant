# Restaurant Order Manager

A console-based Java program demonstrating `ArrayList`, `LinkedList`, `HashMap`,
and `LinkedHashMap`, each used for the job it's best suited to.

## How to run

```
cd src
javac *.java
java Main
```

The menu comes pre-loaded with the sample items from the assignment
(Burger, Pizza, Pasta, Cola) so you can create an order right away.

## Files

- `OrderStatus.java` — enum: `PENDING`, `IN_KITCHEN`, `COMPLETED`, `CANCELLED`.
- `MenuItem.java` — id, name, price, category.
- `OrderItem.java` — a `MenuItem` + quantity, with `calculateSubtotal()`.
- `Order.java` — an order's items, total, and status. `addItem()` /
  `removeItem()` always call `calculateTotal()` internally, so the total is
  never stale. `displayOrder()` only reads the total; it never computes it.
- `Restaurant.java` — owns the four collections and every menu operation.
- `Main.java` — the console menu loop (options 1–15).

## Why each collection was chosen

| Collection | Field | Why |
|---|---|---|
| `ArrayList<MenuItem>` | `menu` | The menu is just a flat list you display/scan in full or by index — no need for fast key lookup or ordered insert/remove at both ends. |
| `LinkedList<Order>` | `kitchenQueue` | Used strictly as a FIFO queue (`addLast` / `peekFirst` / `removeFirst`): the first order sent to the kitchen is the first one processed. `LinkedList` implements `Deque`, so this queue behavior is natural and efficient. |
| `HashMap<Integer, Order>` | `orders` | The permanent record of *every* order ever created (pending, in-kitchen, completed, or cancelled — orders are never removed). Looking an order up by its unique ID needs to be fast (`O(1)`), and insertion order doesn't matter here — the `OrderStatus` field, not the collection, tells you the order's story. |
| `LinkedHashMap<Integer, Order>` | `completedOrders` | Also needs fast lookup by order ID, but *also* needs to remember the order in which orders were completed — plain `HashMap` doesn't guarantee that, but `LinkedHashMap` preserves insertion order automatically. |

## How the lifecycle and cancellation fit together

```
Create Order          -> PENDING           (stored in `orders`)
Add Order to Queue     -> IN_KITCHEN        (added to `kitchenQueue`)
Process Next Order     -> COMPLETED         (removed from queue, added to `completedOrders`)
Cancel Order           -> CANCELLED         (removed from `kitchenQueue` if it was there)
```

At every step the order stays in `orders` (the `HashMap`) — nothing is ever
deleted from it. `Cancel Order` only removes the order from `kitchenQueue` if
it was currently `IN_KITCHEN` (a `PENDING` order was never queued, so there's
nothing to remove there), and a cancelled order is never added to
`completedOrders`, since it wasn't completed.

`Process Next Order` also guards against two edge cases before touching
anything: an empty queue, and an order at the front of the queue with no
items in it (that order is left in place with a message, instead of being
silently marked complete).
