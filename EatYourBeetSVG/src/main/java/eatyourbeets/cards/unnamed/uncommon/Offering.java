package eatyourbeets.cards.unnamed.uncommon;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.OfferingEffect;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.cards.base.UnnamedCard;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.monsters.PlayerMinions.UnnamedDoll;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class Offering extends UnnamedCard
{
    public static final EYBCardData DATA = Register(Offering.class)
            .SetSkill(0, CardRarity.UNCOMMON, EYBCardTarget.None);

    public Offering()
    {
        super(DATA);

        Initialize(0, 0, 6, 2);

        SetExhaust(true);
        SetSummon(true);
    }

    @Override
    protected void OnUpgrade()
    {
        SetInnate(true);
    }

    @Override
    protected void Refresh(AbstractMonster enemy)
    {
        super.Refresh(enemy);

        SetPlayable(GameUtilities.GetHP(player, false, false) > magicNumber);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.VFX(new OfferingEffect(), 0.7f, true);
        GameActions.Bottom.LoseHP(magicNumber, AttackEffects.NONE);
        GameActions.Bottom.SummonDoll(1)
        .AddCallback(dolls ->
        {
            for (UnnamedDoll doll : dolls)
            {
                doll.increaseMaxHp(secondaryValue, true);
            }
        });
    }
}