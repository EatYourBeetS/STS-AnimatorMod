package eatyourbeets.cards.animator.beta.ultrarare;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.*;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.misc.CardMods.AfterLifeMod;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.GameActions;

public class AngelAlter extends AnimatorCard_UltraRare
{
    public static final EYBCardData DATA = Register(AngelAlter.class).SetAttack(0, CardRarity.SPECIAL, EYBAttackType.Piercing).SetColor(CardColor.COLORLESS).SetSeries(CardSeries.AngelBeats);

    public AngelAlter()
    {
        super(DATA);

        Initialize(10, 0, 1, 0);
        SetAffinity_Air(0, 0, 2);
        SetAffinity_Fire(0, 0, 2);
        SetAffinity_Light(2, 0, 2);

        AfterLifeMod.Add(this);
    }

    @Override
    protected void OnUpgrade()
    {
        SetAffinity_Air(0, 0, 3);
        SetAffinity_Fire(0, 0, 3);
        SetAffinity_Light(2, 0, 3);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.DealDamage(this, m, AttackEffects.SLASH_HEAVY);

        GameActions.Bottom.RaiseFireLevel(magicNumber, true);
        GameActions.Bottom.RaiseAirLevel(magicNumber, true);

        GameActions.Bottom.MakeCardInDrawPile(makeStatEquivalentCopy());
        GameActions.Bottom.MakeCardInDiscardPile(makeStatEquivalentCopy());
    }

    @Override
    public boolean cardPlayable(AbstractMonster m)
    {
        return super.cardPlayable(m) && !isInAutoplay && CombatStats.ControlPile.Contains(this) && AfterLifeMod.IsAdded(this);
    }
}