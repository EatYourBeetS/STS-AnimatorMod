package pinacolada.cards.pcl.colorless;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.exordium.*;
import com.megacrit.cardcrawl.orbs.Dark;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.interfaces.delegates.ActionT3;
import eatyourbeets.utilities.TargetHelper;
import pinacolada.actions.special.SelectCreature;
import pinacolada.cards.base.*;
import pinacolada.effects.AttackEffects;
import pinacolada.effects.VFX;
import pinacolada.powers.PCLPowerHelper;
import pinacolada.powers.replacement.PCLCurlUpPower;
import pinacolada.resources.GR;
import pinacolada.resources.pcl.PCLStrings;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameEffects;

import java.util.HashMap;

public class Megaman extends PCLCard
{
    private static class MegamanMove {
        private String text;
        private ActionT3<PCLCard, AbstractPlayer, AbstractMonster> action;
        private int uses;

        public MegamanMove(String text, ActionT3<PCLCard, AbstractPlayer, AbstractMonster> action, int uses) {
            this.text = text;
            this.action = action;
            this.uses = uses;
        }
    }
    private static final PCLStrings.Actions ACTIONS = GR.PCL.Strings.Actions;
    public static final HashMap<String, MegamanMove> EFFECT_MAP = new HashMap<>();
    public static final PCLCardData DATA = Register(Megaman.class).SetAttack(1, CardRarity.RARE, PCLAttackType.Ranged, EYBCardTarget.None)
            .SetMultiformData(3, false).SetColor(CardColor.COLORLESS)
            .SetSeries(CardSeries.Megaman);

    static {
        EFFECT_MAP.put(AcidSlime_L.ID, new MegamanMove(ACTIONS.ApplyToALL(1, GR.Tooltips.Weak, true), (c, p, m) -> PCLActions.Bottom.ApplyWeak(TargetHelper.Enemies(), 1), 2));
        EFFECT_MAP.put(AcidSlime_M.ID, EFFECT_MAP.get(AcidSlime_L.ID));
        EFFECT_MAP.put(AcidSlime_S.ID, EFFECT_MAP.get(AcidSlime_L.ID));
        EFFECT_MAP.put(Cultist.ID, new MegamanMove(ACTIONS.GainAmount(1, GR.Tooltips.Ritual, true), (c, p, m) -> PCLActions.Bottom.StackPower(TargetHelper.Player(), PCLPowerHelper.Ritual, 1), 1));
        EFFECT_MAP.put(FungiBeast.ID, new MegamanMove(ACTIONS.ApplyToALL(1, GR.Tooltips.Vulnerable, true), (c, p, m) -> PCLActions.Bottom.ApplyVulnerable(TargetHelper.Enemies(), 1), 2));
        EFFECT_MAP.put(GremlinFat.ID, new MegamanMove(ACTIONS.ApplyToRandom(2, GR.Tooltips.Weak, true), (c, p, m) -> PCLActions.Bottom.ApplyWeak(TargetHelper.RandomEnemy(), 2), 2));
        EFFECT_MAP.put(GremlinNob.ID, new MegamanMove(ACTIONS.ApplyToRandom(2, GR.Tooltips.Weak, true), (c, p, m) -> PCLActions.Bottom.ApplyWeak(TargetHelper.RandomEnemy(), 2), 2));
        EFFECT_MAP.put(GremlinThief.ID, new MegamanMove(ACTIONS.ApplyToRandom(2, GR.Tooltips.Weak, true), (c, p, m) -> PCLActions.Bottom.ApplyWeak(TargetHelper.RandomEnemy(), 2), 2));
        EFFECT_MAP.put(GremlinTsundere.ID, new MegamanMove(ACTIONS.ApplyToRandom(2, GR.Tooltips.Weak, true), (c, p, m) -> PCLActions.Bottom.ApplyWeak(TargetHelper.RandomEnemy(), 2), 2));
        EFFECT_MAP.put(GremlinWarrior.ID, new MegamanMove(ACTIONS.ApplyToRandom(2, GR.Tooltips.Weak, true), (c, p, m) -> PCLActions.Bottom.ApplyWeak(TargetHelper.RandomEnemy(), 2), 2));
        EFFECT_MAP.put(GremlinWizard.ID, new MegamanMove(ACTIONS.Channel(2, GR.Tooltips.Dark, true), (c, p, m) -> PCLActions.Bottom.ChannelOrbs(Dark::new, 2), 1));
        EFFECT_MAP.put(JawWorm.ID, new MegamanMove(ACTIONS.GainAmount(5, GR.Tooltips.Block, true), (c, p, m) -> PCLActions.Bottom.GainBlock(5), 2));
        EFFECT_MAP.put(LouseDefensive.ID, new MegamanMove(ACTIONS.GainAmount(5, GR.Tooltips.CurlUp, true), (c, p, m) -> PCLActions.Bottom.StackPower(new PCLCurlUpPower(p, 5)), 2));
        EFFECT_MAP.put(LouseNormal.ID, EFFECT_MAP.get(LouseNormal.ID));
        EFFECT_MAP.put(SlaverBlue.ID, new MegamanMove(ACTIONS.ApplyToALL(3, GR.Tooltips.Weak, true), (c, p, m) -> PCLActions.Bottom.ApplyShackles(TargetHelper.Enemies(), 3), 2));
        EFFECT_MAP.put(SpikeSlime_L.ID, new MegamanMove(ACTIONS.ApplyToALL(1, GR.Tooltips.Frail, true), (c, p, m) -> PCLActions.Bottom.ApplyWeak(TargetHelper.Enemies(), 1), 2));
        EFFECT_MAP.put(SpikeSlime_M.ID, EFFECT_MAP.get(SpikeSlime_L.ID));
        EFFECT_MAP.put(SpikeSlime_S.ID, EFFECT_MAP.get(SpikeSlime_L.ID));
    }

    public Megaman()
    {
        super(DATA);

        Initialize(7, 0, 3, 3);
        SetUpgrade(2, 0, 0, 0);

        SetAffinity_Silver(1, 0, 3);
        SetAffinity_Light(1);

        SetUnique(true, -1);

        SetCooldown(1, 0, __ -> {});
    }

    @Override
    protected String GetRawDescription(Object... args)
    {
        return super.GetRawDescription(cardData.Strings.EXTENDED_DESCRIPTION[auxiliaryData.form]);
    }

    @Override
    public void Refresh(AbstractMonster enemy)
    {
        super.Refresh(enemy);

        if (cooldown.GetCurrent() == 0) {
            switch (auxiliaryData.form) {
                case 1:
                    SetAttackType(PCLAttackType.Fire);
                    break;
                case 2:
                    SetAttackType(PCLAttackType.Ice);
                    break;
                default:
                    SetAttackType(PCLAttackType.Electric);
                    break;
            }
        }
        else {
            SetAttackType(PCLAttackType.Ranged);
        }
    }

    @Override
    protected float GetInitialDamage()
    {
        if (cooldown.GetCurrent() == 0) {
            return super.GetInitialDamage() * secondaryValue;
        }
        return super.GetInitialDamage();
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.SelectCreature(SelectCreature.Targeting.Any, this.name).IsCancellable(false).AddCallback((c) -> {
            if (c.isPlayer) {
                cooldown.ProgressCooldown(1);
            } else {
                PCLActions.Bottom.DealCardDamage(this, c, attackType == PCLAttackType.Ranged ? AttackEffects.GUNSHOT : AttackEffects.PSYCHOKINESIS)
                        .forEach(d -> d.SetVFX(true, false)
                                .SetVFXColor(GetVFXColor(), GetVFXColor())
                                .SetDamageEffect(enemy ->
                                        attackType == PCLAttackType.Ranged ? 0.1f : PCLGameEffects.List.Add(VFX.SmallLaser(player.hb, enemy.hb, GetVFXColor())).duration * 0.1f));
            }
        });

    }

    protected Color GetVFXColor() {
        switch (attackType) {
            case Fire:
                return Color.FIREBRICK;
            case Ice:
                return Color.SKY;
            case Electric:
                return Color.GOLDENROD;
            default:
                return Color.WHITE;
        }
    }
}