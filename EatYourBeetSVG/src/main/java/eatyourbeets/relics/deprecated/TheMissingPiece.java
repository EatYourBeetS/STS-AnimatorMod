package eatyourbeets.relics.deprecated;

public class TheMissingPiece extends AbstractMissingPiece
{
    public static final String ID = CreateFullID(TheMissingPiece.class);
    public static final int REWARD_INTERVAL = 4;

    public TheMissingPiece()
    {
        super(ID, RelicTier.DEPRECATED, LandingSound.FLAT);
    }

    @Override
    protected int GetRewardInterval()
    {
        return REWARD_INTERVAL;
    }
}