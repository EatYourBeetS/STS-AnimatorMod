package eatyourbeets.cards.animator.series.OnePunchMan;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.*;
import eatyourbeets.interfaces.markers.MartialArtist;
import eatyourbeets.powers.common.AgilityPower;
import eatyourbeets.utilities.GameActions;

public class SilverFang extends AnimatorCard implements MartialArtist
{
    public static final EYBCardData DATA = Register(SilverFang.class).SetAttack(2, CardRarity.COMMON);

    public SilverFang()
    {
        super(DATA);

        Initialize(8, 3, 1);
        SetUpgrade(3, 1, 0);
        SetScaling(0, 1, 0);

        SetSynergy(Synergies.OnePunchMan);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.GainBlock(block);
        GameActions.Bottom.DealDamage(this, m, AbstractGameAction.AttackEffect.BLUNT_HEAVY);
        AgilityPower.PreserveOnce();

        if (HasSynergy())
        {
            GameActions.Bottom.SelectFromHand(name, 1, true)
            .SetFilter(c -> c instanceof EYBCard && c.type == CardType.ATTACK)
            .AddCallback(cards ->
            {
                if (cards.size() > 0)
                {
                    EYBCard card = (EYBCard)cards.get(0);
                    card.agilityScaling += 1;
                    card.flash();
                }
            });
        }
    }
}