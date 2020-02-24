package eatyourbeets.cards.animator.beta;

import com.megacrit.cardcrawl.actions.unique.ArmamentsAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.powers.animator.WinryRockbellPower;
import eatyourbeets.utilities.GameActions;

public class WinryRockbell extends AnimatorCard
{
    public static final EYBCardData DATA = Register(WinryRockbell.class).SetPower(1, CardRarity.UNCOMMON);

    public WinryRockbell()
    {
        super(DATA);

        Initialize(0, 0, WinryRockbellPower.BLOCK_AMOUNT);
        SetUpgrade(0, 2, 0);

        SetSynergy(Synergies.FullmetalAlchemist);
    }

    @Override
    protected void OnUpgrade()
    {
        SetRetain(true);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        if (upgraded)
        {
            GameActions.Bottom.GainBlock(block);
        }

        GameActions.Bottom.StackPower(new WinryRockbellPower(p, 1));

        if (HasSynergy())
        {
            GameActions.Bottom.SelectFromHand(name, 1, false)
            .SetOptions(true, true, true, false, true, false)
            .SetMessage(ArmamentsAction.TEXT[0])
            .SetFilter(AbstractCard::canUpgrade)
            .AddCallback(cards ->
            {
                if (cards.size() > 0 && cards.get(0).canUpgrade())
                {
                    cards.get(0).upgrade();
                    cards.get(0).flash();
                }
            });
        }
    }
}