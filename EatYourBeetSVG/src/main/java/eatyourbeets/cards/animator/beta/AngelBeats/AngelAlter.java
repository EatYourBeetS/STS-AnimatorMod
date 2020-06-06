package eatyourbeets.cards.animator.beta.AngelBeats;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard_UltraRare;
import eatyourbeets.cards.base.EYBAttackType;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.misc.CardMods.AfterLifeMod;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.GameActions;

public class AngelAlter extends AnimatorCard_UltraRare
{
    public static final EYBCardData DATA = Register(AngelAlter.class).SetAttack(0, CardRarity.SPECIAL, EYBAttackType.Piercing).SetColor(CardColor.COLORLESS);

    public AngelAlter()
    {
        super(DATA);

        Initialize(10, 0, 1, 0);
        SetScaling(0, 2, 2);

        SetSynergy(Synergies.AngelBeats);
        AfterLifeMod.Add(this);
    }

    @Override
    protected void OnUpgrade()
    {
        SetScaling(0, 3, 3);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.DealDamage(this, m, AbstractGameAction.AttackEffect.SLASH_HEAVY);

        GameActions.Bottom.GainForce(magicNumber, true);
        GameActions.Bottom.GainAgility(magicNumber, true);

        GameActions.Bottom.MakeCardInDrawPile(makeStatEquivalentCopy());
        GameActions.Bottom.MakeCardInDiscardPile(makeStatEquivalentCopy());
    }

    @Override
    public boolean cardPlayable(AbstractMonster m)
    {
        return super.cardPlayable(m) && !isInAutoplay && CombatStats.ControlPile.Contains(this) && AfterLifeMod.IsAdded(this);
    }
}