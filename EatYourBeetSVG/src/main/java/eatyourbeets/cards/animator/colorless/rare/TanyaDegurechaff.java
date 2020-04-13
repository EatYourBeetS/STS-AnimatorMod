package eatyourbeets.cards.animator.colorless.rare;

import com.evacipated.cardcrawl.mod.stslib.cards.interfaces.StartupCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.animator.special.TanyaDegurechaff_Type95;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBAttackType;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.cards.base.attributes.AbstractAttribute;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.GameActions;

public class TanyaDegurechaff extends AnimatorCard implements StartupCard
{
    public static final EYBCardData DATA = Register(TanyaDegurechaff.class).SetAttack(2, CardRarity.RARE, EYBAttackType.Ranged).SetColor(CardColor.COLORLESS);
    static
    {
        DATA.AddPreview(new TanyaDegurechaff_Type95(), false);
    }

    public TanyaDegurechaff()
    {
        super(DATA);

        Initialize(4, 6, 1);
        SetUpgrade(2, 2);
        SetScaling(1, 1, 0);

        SetSynergy(Synergies.YoujoSenki);
    }

    @Override
    protected void Refresh(AbstractMonster enemy)
    {
        super.Refresh(enemy);

        magicNumber = (baseMagicNumber + player.hand.getSkills().size());
    }

    @Override
    public AbstractAttribute GetDamageInfo()
    {
        return super.GetDamageInfo().AddMultiplier(magicNumber);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.GainBlock(this.block);
        GameActions.Bottom.DiscardFromHand(name, 999, true)
        .SetOptions(false, true, true)
        .SetFilter(c -> c.type == CardType.SKILL)
        .AddCallback(m, (enemy, cards) ->
        {
            for (int i = 0; i < (cards.size() + baseMagicNumber); i++)
            {
                GameActions.Bottom.SFX("ATTACK_FIRE");
                GameActions.Bottom.DealDamage(this, (AbstractCreature) enemy, AbstractGameAction.AttackEffect.NONE);
            }
        });
    }

    @Override
    public boolean atBattleStartPreDraw()
    {
        if (CombatStats.TryActivateLimited(cardID))
        {
            GameActions.Bottom.MakeCardInDrawPile(new TanyaDegurechaff_Type95());

            return true;
        }

        return false;
    }
}