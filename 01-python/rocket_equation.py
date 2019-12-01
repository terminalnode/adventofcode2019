"""Module to solve Day 1 of Advent of Code 2019."""

with open("input", "r") as input_file:
    masses = [int(i.strip()) for i in input_file.readlines()]


def get_fuel_from_mass(mass):
    """Get amount of fuel we'll need for a given mass."""
    return int(mass / 3 - 2)


def reitterage_get_fuel_from_mass(mass):
    """Get amount of fuel we'll need for a given mass, including fuel mass."""
    current_fuel = 0
    while (mass != 0):
        mass = get_fuel_from_mass(mass)
        if mass < 0:
            mass = 0
        current_fuel += mass
    return current_fuel


problem1 = sum([get_fuel_from_mass(i) for i in masses])
problem2 = sum([reitterage_get_fuel_from_mass(i) for i in masses])

print(f"Solution to part 1: {problem1}")
print(f"Solution to part 2: {problem2}")
