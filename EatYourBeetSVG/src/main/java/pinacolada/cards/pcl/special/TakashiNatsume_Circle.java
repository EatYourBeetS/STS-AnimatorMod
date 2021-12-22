package pinacolada.cards.pcl.special;

import com.evacipated.cardcrawl.mod.stslib.powers.StunMonsterPower;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardQueueItem;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.TargetHelper;
import pinacolada.cards.base.*;
import pinacolada.cards.pcl.colorless.TakashiNatsume;
import pinacolada.cards.pcl.series.GenshinImpact.AyakaKamisato;
import pinacolada.cards.pcl.series.MadokaMagica.*;
import pinacolada.effects.AttackEffects;
import pinacolada.powers.PCLPower;
import pinacolada.powers.common.DeenergizedPower;
import pinacolada.powers.special.InvertPower;
import pinacolada.powers.special.TemporaryThousandCutsPower;
import pinacolada.resources.GR;
import pinacolada.resources.pcl.PCLStrings;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameUtilities;
import pinacolada.utilities.PCLJUtils;

public class TakashiNatsume_Circle extends PCLCard
{
    public enum Form
    {
        None,
        Curse_Delusion,
        Curse_Depression,
        Curse_Eclipse,
        Curse_GriefSeed,
        Curse_Greed,
        Curse_JunTormented,
        Curse_Nutcracker,
        Curse_Slumber,
        Decay,
        Doubt,
        HomuraAkemi_Homulily,
        IrohaTamaki_Giovanna,
        KyokoSakura_Ophelia,
        MadokaKaname_Krimheild,
        MamiTomoe_Candeloro,
        NagisaMomoe_Charlotte,
        Necronomicurse,
        Normality,
        Pain,
        Parasite,
        Regret,
        SayakaMiki_Oktavia,
        Shame,
        TimeParadox
    }

    private static final PCLStrings.Actions ACTIONS = GR.PCL.Strings.Actions;
    private static final int DAMAGE_DECAY = 2;
    private static final int HEAL_NUTCRACKER = 5;
    private static final int NORMALITY_HITS = 3;
    public static final PCLCardData DATA = Register(TakashiNatsume_Circle.class).SetSkill(0, CardRarity.SPECIAL, eatyourbeets.cards.base.EYBCardTarget.None).SetColor(CardColor.COLORLESS).SetSeries(CardSeries.NatsumeYuujinchou);
    private TakashiNatsume_Circle.Form currentForm = Form.None;

    public TakashiNatsume_Circle() {
        this(Form.None);
    }

    public TakashiNatsume_Circle(Form form)
    {
        super(DATA);

        Initialize(0, 0, 1, 2);
        SetUpgrade(0, 0, 1, 1);

        SetAffinity_Dark(1);

        SetPurge(true);
        SetHealing(true);

        ChangeForm(form);
    }

    public void ChangeForm(TakashiNatsume_Circle.Form form) {
        currentForm = form;
        switch (form) {
            case Curse_Delusion:
                cardText.OverrideDescription(DATA.Strings.DESCRIPTION + " NL  NL " + DATA.Strings.EXTENDED_DESCRIPTION[3], true);
                break;
            case Curse_Depression:
                cardText.OverrideDescription(DATA.Strings.DESCRIPTION + " NL  NL " + ACTIONS.Draw(secondaryValue, true), true);
                break;
            case Curse_Eclipse:
                cardText.OverrideDescription(DATA.Strings.DESCRIPTION + " NL  NL " +  ACTIONS.Play(AyakaKamisato.DATA.Strings.NAME, true), true);
                break;
            case Curse_Greed:
                cardText.OverrideDescription(DATA.Strings.DESCRIPTION + " NL  NL " + ACTIONS.Motivate(secondaryValue, true), true);
                break;
            case Curse_GriefSeed:
            case Decay:
                baseDamage = DAMAGE_DECAY * secondaryValue;
                SetAttackType(PCLAttackType.Elemental);
                SetAttackTarget(eatyourbeets.cards.base.EYBCardTarget.ALL);
                this.type = CardType.ATTACK;
                break;
            case Curse_Nutcracker:
                cardText.OverrideDescription(DATA.Strings.DESCRIPTION + " NL  NL " + ACTIONS.HealHP(HEAL_NUTCRACKER + magicNumber, false), true);
                break;
            case Curse_JunTormented:
                cardText.OverrideDescription(DATA.Strings.DESCRIPTION + " NL  NL " + ACTIONS.ApplyToALL(secondaryValue, GR.Tooltips.Weak, true) + " NL  NL " + ACTIONS.ApplyToALL(magicNumber, GR.Tooltips.Vulnerable, true), true);
                break;
            case Curse_Slumber:
                cardText.OverrideDescription(DATA.Strings.DESCRIPTION + " NL  NL " + PCLJUtils.Format(DATA.Strings.EXTENDED_DESCRIPTION[5],secondaryValue), true);
                break;
            case Doubt:
                cardText.OverrideDescription(DATA.Strings.DESCRIPTION + " NL  NL " + ACTIONS.ApplyToALL(secondaryValue, GR.Tooltips.Weak, true), true);
                break;
            case HomuraAkemi_Homulily:
                cardText.OverrideDescription(DATA.Strings.DESCRIPTION + " NL  NL " +  ACTIONS.Play(HomuraAkemi.DATA.Strings.NAME, true), true);
                break;
            case IrohaTamaki_Giovanna:
                cardText.OverrideDescription(DATA.Strings.DESCRIPTION + " NL  NL " +  ACTIONS.Play(IrohaTamaki.DATA.Strings.NAME, true), true);
                break;
            case KyokoSakura_Ophelia:
                cardText.OverrideDescription(DATA.Strings.DESCRIPTION + " NL  NL " +  ACTIONS.Play(KyokoSakura.DATA.Strings.NAME, true), true);
                break;
            case MadokaKaname_Krimheild:
                cardText.OverrideDescription(DATA.Strings.DESCRIPTION + " NL  NL " +  ACTIONS.Play(MadokaKaname.DATA.Strings.NAME, true), true);
                break;
            case MamiTomoe_Candeloro:
                cardText.OverrideDescription(DATA.Strings.DESCRIPTION + " NL  NL " +  ACTIONS.Play(MamiTomoe.DATA.Strings.NAME, true), true);
                break;
            case NagisaMomoe_Charlotte:
                cardText.OverrideDescription(DATA.Strings.DESCRIPTION + " NL  NL " +  ACTIONS.Play(NagisaMomoe.DATA.Strings.NAME, true), true);
                break;
            case Necronomicurse:
                cardText.OverrideDescription(DATA.Strings.DESCRIPTION + " " + ACTIONS.NextTurnDraw(secondaryValue, true) + " NL  NL " + DATA.Strings.EXTENDED_DESCRIPTION[4], true);
                break;
            case Normality:
                this.cost = 1;
                cardText.OverrideDescription(DATA.Strings.DESCRIPTION + " NL  NL " + PCLJUtils.Format(DATA.Strings.EXTENDED_DESCRIPTION[0],NORMALITY_HITS), true);
                break;
            case Pain:
                cardText.OverrideDescription(DATA.Strings.DESCRIPTION + " NL  NL " + PCLJUtils.Format(DATA.Strings.EXTENDED_DESCRIPTION[2],secondaryValue), true);
                break;
            case SayakaMiki_Oktavia:
                cardText.OverrideDescription(DATA.Strings.DESCRIPTION + " NL  NL " +  ACTIONS.Play(SayakaMiki.DATA.Strings.NAME, true), true);
                break;
            case Regret:
                baseDamage = magicNumber;
                SetAttackType(PCLAttackType.Elemental);
                SetAttackTarget(eatyourbeets.cards.base.EYBCardTarget.Random);
                this.type = CardType.ATTACK;
                cardText.OverrideDescription(DATA.Strings.DESCRIPTION + " NL  NL " + DATA.Strings.EXTENDED_DESCRIPTION[1], true);
                break;
            case Shame:
                cardText.OverrideDescription(DATA.Strings.DESCRIPTION + " NL  NL " + ACTIONS.ApplyToALL(secondaryValue, GR.Tooltips.Frail, true), true);
                break;
            default:
                cardText.OverrideDescription(DATA.Strings.DESCRIPTION + " NL  NL " + ACTIONS.NextTurnDraw(secondaryValue, true), true);
        }
    }

    @Override
    public void triggerOnEndOfTurnForPlayingCard()
    {
        AbstractDungeon.actionManager.cardQueue.add(new CardQueueItem(this, true));

        if (EnergyPanel.totalCount < this.costForTurn) {
            PCLActions.Bottom.StackPower(new DeenergizedPower(player, this.costForTurn - EnergyPanel.totalCount));
        }
    }


    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        int[] damageMatrix;
        switch(currentForm) {
            case Curse_Delusion:
                PCLActions.Bottom.ModifyTag(player.drawPile, 999, AUTOPLAY, false);
                break;
            case Curse_Depression:
                PCLActions.Bottom.Draw(secondaryValue);
                break;
            case Curse_Eclipse:
            case HomuraAkemi_Homulily:
            case IrohaTamaki_Giovanna:
            case KyokoSakura_Ophelia:
            case MadokaKaname_Krimheild:
            case MamiTomoe_Candeloro:
            case NagisaMomoe_Charlotte:
            case SayakaMiki_Oktavia:
                AbstractCard c = GetCardForEffect();
                if (upgraded) {
                    c.upgrade();
                }
                c.applyPowers();
                if (PCLGameUtilities.IsPlayerTurn()) {
                    PCLActions.Bottom.PlayCopy(c, null);
                }
                else {
                    c.use(player, null);
                }
                break;
            case Curse_JunTormented:
                PCLActions.Bottom.ApplyWeak(TargetHelper.Enemies(), secondaryValue);
                PCLActions.Bottom.ApplyFrail(TargetHelper.Enemies(), magicNumber);
                break;
            case Curse_Greed:
                PCLActions.Bottom.Motivate(secondaryValue);
                break;
            case Curse_Nutcracker:
                PCLActions.Bottom.Heal(HEAL_NUTCRACKER + magicNumber);
                break;
            case Decay:
            case Curse_GriefSeed:
                damageMatrix = DamageInfo.createDamageMatrix(damage, true);
                PCLActions.Bottom.DealDamageToAll(damageMatrix, DamageInfo.DamageType.THORNS, AttackEffects.FIRE);
                break;
            case Curse_Slumber:
                PCLActions.Bottom.StackPower(player, new InvertPower(player, secondaryValue));
                break;
            case Doubt:
                PCLActions.Bottom.ApplyWeak(TargetHelper.Enemies(), secondaryValue);
                break;
            case Necronomicurse:
                PCLActions.Bottom.DrawNextTurn(secondaryValue);
                if (CombatStats.TryActivateLimited(cardID)) {
                    PCLActions.Bottom.SelectFromPile(name,999,player.exhaustPile).SetFilter(card -> card.cardID.equals(TakashiNatsume.DATA.ID)).AddCallback(
                            cards -> {
                                for (AbstractCard card : cards) {
                                    PCLActions.Bottom.MoveCard(card,player.drawPile).AddCallback(cCard -> {
                                        PCLActions.Bottom.Motivate(card, 1);
                                        ((TakashiNatsume) card).SetAfterlife(true);
                                    });
                                }
                            }
                    );
                }
                break;
            case Normality:
                PCLActions.Bottom.StackPower(player, new TakashiNatsumeCirclePower(player, NORMALITY_HITS));
                break;
            case Pain:
                PCLActions.Bottom.StackPower(player, new TemporaryThousandCutsPower(player, secondaryValue));
                break;
            case Regret:
                for (int i = 0; i < player.hand.size(); i++) {
                    PCLActions.Bottom.DealCardDamageToRandomEnemy(this, AttackEffects.FIRE);
                }
                break;
            case Shame:
                PCLActions.Bottom.ApplyFrail(TargetHelper.Enemies(), secondaryValue);
                break;
            default:
                PCLActions.Bottom.DrawNextTurn(secondaryValue);
                break;
        }
    }

    protected AbstractCard GetCardForEffect() {
        switch(currentForm) {
            case Curse_Eclipse:
                return new AyakaKamisato();
            case HomuraAkemi_Homulily:
                return new HomuraAkemi_Homulily();
            case IrohaTamaki_Giovanna:
                return new IrohaTamaki_Giovanna();
            case KyokoSakura_Ophelia:
                return new KyokoSakura_Ophelia();
            case MadokaKaname_Krimheild:
                return new MadokaKaname_Krimheild();
            case MamiTomoe_Candeloro:
                return new MamiTomoe_Candeloro();
            case NagisaMomoe_Charlotte:
                return new NagisaMomoe_Charlotte();
            case SayakaMiki_Oktavia:
                return new SayakaMiki_Oktavia();
            default:
                return null;
        }
    }

    public static class TakashiNatsumeCirclePower extends PCLPower
    {
        public TakashiNatsumeCirclePower(AbstractPlayer owner, int amount)
        {
            super(owner, TakashiNatsume_Circle.DATA);

            this.amount = amount;
            updateDescription();
        }

        @Override
        public int onAttacked(DamageInfo info, int damageAmount)
        {
            if (this.amount > 0 && info.owner instanceof AbstractMonster && info.type != DamageInfo.DamageType.THORNS && info.type != DamageInfo.DamageType.HP_LOSS)
            {
                PCLActions.Top.ApplyPower(this.owner, new StunMonsterPower((AbstractMonster) info.owner, 1));
                this.amount -= 1;
                this.flash();
            }

            return super.onAttacked(info, damageAmount);
        }

        @Override
        public void atEndOfRound() {
            PCLActions.Bottom.RemovePower(owner, owner, this);
        }

        @Override
        public void updateDescription()
        {
            description = FormatDescription(0, amount);
        }
    }
}