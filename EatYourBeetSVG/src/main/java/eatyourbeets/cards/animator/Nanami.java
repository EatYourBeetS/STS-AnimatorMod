package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.relics.ChemicalX;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import eatyourbeets.resources.Resources_Animator;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.Synergies;
import eatyourbeets.misc.NanamiEffects.*;

public class Nanami extends AnimatorCard
{
    public static final String ID = CreateFullID(Nanami.class.getSimpleName());
    public static final String[] DESCRIPTIONS = Resources_Animator.GetCardStrings(ID).EXTENDED_DESCRIPTION;

    private AbstractMonster lastTarget = null;
    private AbstractMonster target = null;

    public Nanami()
    {
        super(ID, -1, CardType.SKILL, CardRarity.UNCOMMON, CardTarget.ENEMY);

        Initialize(5,4, 3);

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
        this.energyOnUse = EnergyPanel.totalCount;
        if (p.hasRelic(ChemicalX.ID))
        {
            // This should be illegal
            this.energyOnUse += ChemicalX.BOOST;
        }

        switch (m.intent)
        {
            case ATTACK:
                NanamiEffect_Attack.Execute(p, m, this);
                break;

            case ATTACK_BUFF:
                NanamiEffect_Attack_Buff.Execute(p, m, this);
                break;

            case ATTACK_DEBUFF:
                NanamiEffect_Attack_Debuff.Execute(p, m, this);
                break;

            case ATTACK_DEFEND:
                NanamiEffect_Attack_Defend.Execute(p, m, this);
                break;

            case BUFF:
                NanamiEffect_Buff.Execute(p, m, this);
                break;

            case DEBUFF:
                NanamiEffect_Debuff.Execute(p, m, this);
                break;

            case STRONG_DEBUFF:
                NanamiEffect_Strong_Debuff.Execute(p, m, this);
                break;

            case DEFEND:
                NanamiEffect_Defend.Execute(p, m, this);
                break;

            case DEFEND_DEBUFF:
                NanamiEffect_Defend_Debuff.Execute(p, m, this);
                break;

            case DEFEND_BUFF:
                NanamiEffect_Defend_Buff.Execute(p, m, this);
                break;

            case ESCAPE:
                NanamiEffect_Escape.Execute(p, m, this);
                break;

            case SLEEP:
                NanamiEffect_Sleep.Execute(p, m, this);
                break;

            case STUN:
                NanamiEffect_Stun.Execute(p, m, this);
                break;

            case UNKNOWN:
                NanamiEffect_Unknown.Execute(p, m, this);
                break;

            case DEBUG:
            case NONE:
                NanamiEffect_None.Execute(p, m, this);
                break;

            case MAGIC:
            default:
                NanamiEffect_Magic.Execute(p, m, this);
                break;
        }

        if (!this.freeToPlayOnce)
        {
            p.energy.use(EnergyPanel.totalCount);
        }
    }

    @Override
    public void upgrade() 
    {
        if (TryUpgrade())
        {
            upgradeDamage(1);
            upgradeBlock(1);
            upgradeMagicNumber(1);
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

        this.energyOnUse = EnergyPanel.totalCount;
        if (AbstractDungeon.player.hasRelic(ChemicalX.ID))
        {
            this.energyOnUse += ChemicalX.BOOST;
        }

        switch (monster.intent)
        {
            case ATTACK:
                NanamiEffect_Attack.UpdateDescription(this);
                break;

            case ATTACK_BUFF:
                NanamiEffect_Attack_Buff.UpdateDescription(this);
                break;

            case ATTACK_DEBUFF:
                NanamiEffect_Attack_Debuff.UpdateDescription(this);
                break;

            case ATTACK_DEFEND:
                NanamiEffect_Attack_Defend.UpdateDescription(this);
                break;

            case BUFF:
                NanamiEffect_Buff.UpdateDescription(this);
                break;

            case DEBUFF:
                NanamiEffect_Debuff.UpdateDescription(this);
                break;

            case STRONG_DEBUFF:
                NanamiEffect_Strong_Debuff.UpdateDescription(this);
                break;

            case DEFEND:
                NanamiEffect_Defend.UpdateDescription(this);
                break;

            case DEFEND_DEBUFF:
                NanamiEffect_Defend_Debuff.UpdateDescription(this);
                break;

            case DEFEND_BUFF:
                NanamiEffect_Defend_Buff.UpdateDescription(this);
                break;

            case ESCAPE:
                NanamiEffect_Escape.UpdateDescription(this);
                break;

            case SLEEP:
                NanamiEffect_Sleep.UpdateDescription(this);
                break;

            case STUN:
                NanamiEffect_Stun.UpdateDescription(this);
                break;

            case UNKNOWN:
                NanamiEffect_Unknown.UpdateDescription(this);
                break;

            case DEBUG:
            case NONE:
                NanamiEffect_None.UpdateDescription(this);
                break;

            case MAGIC:
            default:
                NanamiEffect_Magic.UpdateDescription(this);
                break;
        }

        initializeDescription();
    }
}