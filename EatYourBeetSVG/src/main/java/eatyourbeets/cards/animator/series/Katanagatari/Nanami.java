package eatyourbeets.cards.animator.series.Katanagatari;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.relics.ChemicalX;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.misc.NanamiEffects.*;
import eatyourbeets.ui.cards.TargetEffectPreview;
import eatyourbeets.resources.animator.AnimatorResources;
import eatyourbeets.utilities.GameUtilities;

public class Nanami extends AnimatorCard
{
    public static final String ID = Register(Nanami.class);
    public static final String[] DESCRIPTIONS = AnimatorResources.GetCardStrings(ID).EXTENDED_DESCRIPTION;

    private final TargetEffectPreview targetEffectPreview = new TargetEffectPreview(this::updateCurrentEffect);

    public Nanami()
    {
        super(ID, -1, CardRarity.UNCOMMON, CardType.SKILL, CardTarget.ENEMY);

        Initialize(5, 4, 3);
        SetUpgrade(1, 1, 1);

        SetExhaust(true);
        SetSynergy(Synergies.Katanagatari);
    }


    @Override
    public void calculateCardDamage(AbstractMonster mo)
    {
        super.calculateCardDamage(mo);

        targetDrawScale = 1f;
        target_x = Settings.WIDTH * 0.4f;
        target_y = Settings.HEIGHT * 0.4f;
        targetEffectPreview.SetCurrentTarget(mo);
    }

    @Override
    public void update()
    {
        super.update();

        targetEffectPreview.Update();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        this.energyOnUse = GameUtilities.UseEnergyXCost(this);

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
    }

    private void updateCurrentEffect(AbstractMonster monster)
    {
        if (monster == null)
        {
            cardText.OverrideDescription(null, true);

            return;
        }

        this.energyOnUse = EnergyPanel.totalCount;
        if (player.hasRelic(ChemicalX.ID))
        {
            this.energyOnUse += ChemicalX.BOOST;
        }

        switch (monster.intent)
        {
            case ATTACK:
                cardText.OverrideDescription(NanamiEffect_Attack.UpdateDescription(this), true);
                break;

            case ATTACK_BUFF:
                cardText.OverrideDescription(NanamiEffect_Attack_Buff.UpdateDescription(this), true);
                break;

            case ATTACK_DEBUFF:
                cardText.OverrideDescription(NanamiEffect_Attack_Debuff.UpdateDescription(this), true);
                break;

            case ATTACK_DEFEND:
                cardText.OverrideDescription(NanamiEffect_Attack_Defend.UpdateDescription(this), true);
                break;

            case BUFF:
                cardText.OverrideDescription(NanamiEffect_Buff.UpdateDescription(this), true);
                break;

            case DEBUFF:
                cardText.OverrideDescription(NanamiEffect_Debuff.UpdateDescription(this), true);
                break;

            case STRONG_DEBUFF:
                cardText.OverrideDescription(NanamiEffect_Strong_Debuff.UpdateDescription(this), true);
                break;

            case DEFEND:
                cardText.OverrideDescription(NanamiEffect_Defend.UpdateDescription(this), true);
                break;

            case DEFEND_DEBUFF:
                cardText.OverrideDescription(NanamiEffect_Defend_Debuff.UpdateDescription(this), true);
                break;

            case DEFEND_BUFF:
                cardText.OverrideDescription(NanamiEffect_Defend_Buff.UpdateDescription(this), true);
                break;

            case ESCAPE:
                cardText.OverrideDescription(NanamiEffect_Escape.UpdateDescription(this), true);
                break;

            case SLEEP:
                cardText.OverrideDescription(NanamiEffect_Sleep.UpdateDescription(this), true);
                break;

            case STUN:
                cardText.OverrideDescription(NanamiEffect_Stun.UpdateDescription(this), true);
                break;

            case UNKNOWN:
                cardText.OverrideDescription(NanamiEffect_Unknown.UpdateDescription(this), true);
                break;

            case DEBUG:
            case NONE:
                cardText.OverrideDescription(NanamiEffect_None.UpdateDescription(this), true);
                break;

            case MAGIC:
            default:
                cardText.OverrideDescription(NanamiEffect_Magic.UpdateDescription(this), true);
                break;
        }
    }

}