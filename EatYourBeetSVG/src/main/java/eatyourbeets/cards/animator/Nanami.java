package eatyourbeets.cards.animator;

import com.evacipated.cardcrawl.mod.stslib.powers.StunMonsterPower;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.Dark;
import com.megacrit.cardcrawl.orbs.Frost;
import com.megacrit.cardcrawl.orbs.Lightning;
import com.megacrit.cardcrawl.powers.PoisonPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.megacrit.cardcrawl.powers.WeakPower;
import eatyourbeets.GameActionsHelper;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.Synergies;

public class Nanami extends AnimatorCard
{
    public static final String ID = CreateFullID(Nanami.class.getSimpleName());

    private AbstractMonster lastTarget = null;
    private AbstractMonster target = null;

    public Nanami()
    {
        super(ID, 1, CardType.SKILL, CardRarity.UNCOMMON, CardTarget.ENEMY);

        Initialize(8,7, 6);

        SetSynergy(Synergies.Katanagatari);
    }

    @Override
    public void calculateCardDamage(AbstractMonster mo)
    {
        super.calculateCardDamage(mo);

        target = mo;
    }

    @Override
    public void update()
    {
        super.update();

        if (lastTarget != target)
        {
            updateCurrentEffect(target);

            lastTarget = target;
        }

        target = null;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) 
    {
        int stack = upgraded ? 3 : 2;
        switch (m.intent)
        {
            case ATTACK:
                GameActionsHelper.GainBlock(p, this.block);
                break;

            case ATTACK_BUFF:
                GameActionsHelper.GainBlock(p, this.block);
                GameActionsHelper.ApplyPower(p, p, new StrengthPower(p, 1), 1);
                break;

            case ATTACK_DEBUFF:
                GameActionsHelper.GainBlock(p, this.block);
                GameActionsHelper.ApplyPower(p, m, new WeakPower(m, 1, false), 1);
                break;

            case ATTACK_DEFEND:
                GameActionsHelper.GainBlock(p, this.block);
                GameActionsHelper.DamageTarget(p, m, this.damage, this.damageTypeForTurn, AbstractGameAction.AttackEffect.NONE);
                break;

            case BUFF:
                GameActionsHelper.ApplyPower(p, p, new StrengthPower(p, stack), stack);
                break;

            case DEBUFF:
                GameActionsHelper.ApplyPower(p, m, new WeakPower(m, stack, false), stack);
                GameActionsHelper.ApplyPower(p, m, new VulnerablePower(m, stack, false), stack);
                break;

            case STRONG_DEBUFF:
                GameActionsHelper.ApplyPower(p, m, new PoisonPower(m, p, this.magicNumber), this.magicNumber);
                GameActionsHelper.ApplyPower(p, m, new WeakPower(m, stack, false), stack);
                GameActionsHelper.ApplyPower(p, m, new VulnerablePower(m, stack, false), stack);
                break;

            case DEFEND:
                GameActionsHelper.DamageTarget(p, m, this.damage, this.damageTypeForTurn, AbstractGameAction.AttackEffect.NONE);
                break;

            case DEFEND_DEBUFF:
                GameActionsHelper.DamageTarget(p, m, this.damage, this.damageTypeForTurn, AbstractGameAction.AttackEffect.NONE);
                GameActionsHelper.ApplyPower(p, m, new VulnerablePower(m, 1, false), 1);
                break;

            case DEFEND_BUFF:
                GameActionsHelper.DamageTarget(p, m, this.damage, this.damageTypeForTurn, AbstractGameAction.AttackEffect.NONE);
                GameActionsHelper.ApplyPower(p, p, new StrengthPower(p, 1), 1);
                break;

            case ESCAPE:
                GameActionsHelper.ApplyPower(p, m, new StunMonsterPower(m, 1), 1);
                break;

            case MAGIC:
                GameActionsHelper.ChannelOrb(new Lightning(), true);
                GameActionsHelper.ChannelOrb(new Dark(), true);
                GameActionsHelper.ChannelOrb(new Frost(), true);
                break;

            case SLEEP:
                GameActionsHelper.ApplyPower(p, m, new PoisonPower(m, p, this.magicNumber), this.magicNumber);
                break;

            case STUN:
                GameActionsHelper.DamageTarget(p, m, this.damage, this.damageTypeForTurn, AbstractGameAction.AttackEffect.NONE);
                GameActionsHelper.ApplyPower(p, m, new VulnerablePower(m, stack, false), stack);
                break;

            case UNKNOWN:
                GameActionsHelper.GainEnergy(1);
                GameActionsHelper.GainTemporaryHP(p, p, this.magicNumber);
                break;

            case DEBUG:
            case NONE:
                GameActionsHelper.DrawCard(p, this.magicNumber);
                break;
        }
    }

    @Override
    public void upgrade() 
    {
        if (TryUpgrade())
        {
            upgradeDamage(2);
            upgradeBlock(2);
            upgradeMagicNumber(2);
        }
    }

    private void updateCurrentEffect(AbstractMonster monster)
    {
        if (monster == null)
        {
            if (upgraded)
            {
                rawDescription = cardStrings.UPGRADE_DESCRIPTION;
            }
            else
            {
                rawDescription = cardStrings.DESCRIPTION;
            }

            initializeDescription();

            return;
        }

        String[] desc = cardStrings.EXTENDED_DESCRIPTION;

        String stack = upgraded ? "3" : "2";

        switch (monster.intent)
        {
            case ATTACK:
                rawDescription = desc[1];
                //GameActionsHelper.GainBlock(p, this.block);
                break;

            case ATTACK_BUFF:
                rawDescription = desc[1] + " NL " + desc[2].replace("#","1");
                //GameActionsHelper.GainBlock(p, this.block);
                //GameActionsHelper.ApplyPower(p, p, new StrengthPower(p, 1), 1);
                break;

            case ATTACK_DEBUFF:
                rawDescription = desc[1] + " NL " + desc[3].replace("#", "1");
                //GameActionsHelper.GainBlock(p, this.block);
                //GameActionsHelper.ApplyPower(p, m, new WeakPower(m, 1, false), 1);
                break;

            case ATTACK_DEFEND:
                rawDescription = desc[1] + " NL " + desc[0];
                //GameActionsHelper.GainBlock(p, this.block);
                //GameActionsHelper.DamageTarget(p, m, this.damage, this.damageTypeForTurn, AbstractGameAction.AttackEffect.NONE);
                break;

            case BUFF:
                rawDescription = desc[2].replace("#", stack);
                //GameActionsHelper.ApplyPower(p, p, new StrengthPower(p, 2), 2);
                break;

            case DEBUFF:
                rawDescription = desc[3].replace("#", stack) + " NL " + desc[4].replace("#", stack);
                //GameActionsHelper.ApplyPower(p, m, new WeakPower(m, 2, false), 2);
                //GameActionsHelper.ApplyPower(p, m, new VulnerablePower(m, 2, false), 2);
                break;

            case STRONG_DEBUFF:
                rawDescription = desc[9] + " NL " + desc[3].replace("#", stack) + " NL " + desc[4].replace("#", stack);
                //GameActionsHelper.ApplyPower(p, m, new PoisonPower(m, p, this.magicNumber), this.magicNumber);
                //GameActionsHelper.ApplyPower(p, m, new WeakPower(m, 2, false), 2);
                //GameActionsHelper.ApplyPower(p, m, new VulnerablePower(m, 2, false), 2);
                break;

            case DEFEND:
                rawDescription = desc[0];
                //GameActionsHelper.DamageTarget(p, m, this.damage, this.damageTypeForTurn, AbstractGameAction.AttackEffect.NONE);
                break;

            case DEFEND_DEBUFF:
                rawDescription = desc[0] + " NL " + desc[4].replace("#", "1");
                //GameActionsHelper.DamageTarget(p, m, this.damage, this.damageTypeForTurn, AbstractGameAction.AttackEffect.NONE);
                //GameActionsHelper.ApplyPower(p, m, new VulnerablePower(m, 1, false), 1);
                break;

            case DEFEND_BUFF:
                rawDescription = desc[0] + " NL " + desc[2].replace("#", "1");
                //GameActionsHelper.DamageTarget(p, m, this.damage, this.damageTypeForTurn, AbstractGameAction.AttackEffect.NONE);
                //GameActionsHelper.ApplyPower(p, p, new StrengthPower(p, 1), 1);
                break;

            case ESCAPE:
                rawDescription = desc[5];
                //GameActionsHelper.ApplyPower(p, m, new StunMonsterPower(m, 1), 1);
                break;

            case MAGIC:
                rawDescription = desc[6] + " NL " + desc[7] + " NL " + desc[8];
                //GameActionsHelper.ChannelOrb(new Lightning(), true);
                //GameActionsHelper.ChannelOrb(new Dark(), true);
                //GameActionsHelper.ChannelOrb(new Frost(), true);
                break;

            case SLEEP:
                rawDescription = desc[9];
                //GameActionsHelper.ApplyPower(p, m, new PoisonPower(m, p, this.magicNumber), this.magicNumber);
                break;

            case STUN:
                rawDescription = desc[0] + " NL " + desc[4].replace("#", stack);
                //GameActionsHelper.DamageTarget(p, m, this.damage, this.damageTypeForTurn, AbstractGameAction.AttackEffect.NONE);
                //GameActionsHelper.ApplyPower(p, m, new VulnerablePower(m, 2, false), 2);
                break;

            case UNKNOWN:
                rawDescription = desc[10] + " NL " + desc[11];
                //GameActionsHelper.GainEnergy(1);
                //GameActionsHelper.GainTemporaryHP(p, p, this.magicNumber);
                break;

            case DEBUG:
            case NONE:
                rawDescription = desc[12];
                //GameActionsHelper.DrawCard(p, 1);
                break;
        }

        initializeDescription();
    }
}