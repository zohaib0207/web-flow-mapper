type StatsCardProps = {
  title: string;
  value: number;
};

export default function StatsCard({
  title,
  value,
}: StatsCardProps) {
  return (
    <div className="bg-zinc-900 rounded-xl p-4">
      <h3 className="text-zinc-400">
        {title}
      </h3>

      <p className="text-3xl font-bold">
        {value}
      </p>
    </div>
  );
}