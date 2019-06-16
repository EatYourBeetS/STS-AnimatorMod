package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.StrengthPower;
import eatyourbeets.utilities.GameActionsHelper;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.Synergies;

public class Mitsurugi extends AnimatorCard
{
    public static final String ID = CreateFullID(Mitsurugi.class.getSimpleName());

    public Mitsurugi()
    {
        super(ID, 0, CardType.ATTACK, CardRarity.COMMON, CardTarget.ENEMY);

        Initialize(9, 0, 2);

        SetSynergy(Synergies.Konosuba);
    }

    @Override
    public void triggerOnExhaust()
    {
        super.triggerOnExhaust();
        AbstractPlayer player = AbstractDungeon.player;
        GameActionsHelper.ApplyPower(player, player, new StrengthPower(player, this.magicNumber), this.magicNumber);
    }

    @Override
    public boolean canUse(AbstractPlayer p, AbstractMonster m)
    {
        if (super.canUse(p, m))
        {
            if (m == null)
            {
                return true;
            }
            else if ((m.intent == AbstractMonster.Intent.ATTACK || m.intent == AbstractMonster.Intent.ATTACK_BUFF
                    || m.intent == AbstractMonster.Intent.ATTACK_DEFEND || m.intent == AbstractMonster.Intent.ATTACK_DEBUFF))
            {
                return true;
            }
            else
            {
                this.cantUseMessage = cardStrings.EXTENDED_DESCRIPTION[0];

                return false;
            }
        }

        return false;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActionsHelper.DamageTarget(p, m, this.damage, this.damageTypeForTurn, AbstractGameAction.AttackEffect.SLASH_HEAVY);
    }

    @Override
    public void upgrade()
    {
        if (TryUpgrade())
        {
            upgradeDamage(5);
        }
    }
}