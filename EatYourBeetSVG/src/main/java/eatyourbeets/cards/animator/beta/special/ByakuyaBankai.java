package eatyourbeets.cards.animator.beta.special;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.BorderLongFlashEffect;
import com.megacrit.cardcrawl.vfx.combat.ShockWaveEffect;
import eatyourbeets.cards.base.*;
import eatyourbeets.interfaces.delegates.ActionT3;
import eatyourbeets.resources.GR;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class ByakuyaBankai extends AnimatorCard
{
    public static final EYBCardData DATA = Register(ByakuyaBankai.class).SetAttack(-1, CardRarity.SPECIAL, EYBAttackType.Ranged, EYBCardTarget.ALL).SetSeries(CardSeries.Bleach);

    public ByakuyaBankai()
    {
        super(DATA);

        Initialize(7, 5);
        SetUpgrade(2, 2);
        SetAffinity_Red(1, 0, 0);
        SetAffinity_Green(2, 0, 0);

        SetUnique(true, true);
        SetExhaust(true);
        SetMultiDamage(true);
    }

    @Override
    public void triggerOnExhaust()
    {
        super.triggerOnExhaust();
        GameActions.Bottom.MoveCard(this, player.exhaustPile, player.discardPile)
                .ShowEffect(false, false);
        if (this.canUpgrade())
        {
            this.upgrade();
            this.flash();
        }
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        int stacks = GameUtilities.UseXCostEnergy(this);

        for (int i=0; i< stacks; i++)
        {
            GameActions.Bottom.Callback(card ->
                ChooseAction(m)
            );
        }
    }

    private void ChooseAction(AbstractMonster m)
    {
        AnimatorCard damage = GenerateInternal(CardType.ATTACK, this::DamageEffect).Build();
        AnimatorCard block = GenerateInternal(CardType.SKILL, this::BlockEffect).Build();

        CardGroup choices = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
        choices.addToTop(damage);
        choices.addToTop(block);

        Execute(choices, m);
    }

    private AnimatorCardBuilder GenerateInternal(AbstractCard.CardType type, ActionT3<AnimatorCard, AbstractPlayer, AbstractMonster> onUseAction)
    {
        AnimatorCardBuilder builder = new AnimatorCardBuilder(ByakuyaBankai.DATA.ID);
        builder.SetText(name, "", "");
        builder.SetProperties(type, GR.Enums.Cards.THE_ANIMATOR, AbstractCard.CardRarity.RARE, CardTarget.ENEMY);
        builder.SetOnUse(onUseAction);

        if (type.equals(CardType.ATTACK))
        {
            builder.SetAttackType(EYBAttackType.Ranged, EYBCardTarget.ALL);
            builder.SetNumbers(damage, 0, 0, 0);
        }
        else
        {
            builder.SetNumbers(0, block, 0, 0);
        }

        return builder;
    }

    private void Execute(CardGroup group, AbstractMonster m)
    {
        GameActions.Top.SelectFromPile(name, 1, group)
                .SetOptions(false, false)
                .AddCallback(cards ->
                {
                    AbstractCard card = cards.get(0);
                    card.applyPowers();
                    card.calculateCardDamage(m);
                    card.use(player, m);
                });
    }

    private void DamageEffect(AbstractCard card, AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.VFX(new BorderLongFlashEffect(Color.WHITE));
        GameActions.Bottom.VFX(new ShockWaveEffect(p.hb.cX, p.hb.cY, Color.WHITE, ShockWaveEffect.ShockWaveType.ADDITIVE), 0.75f);

        GameActions.Bottom.DealDamageToAll(this, AbstractGameAction.AttackEffect.SLASH_HEAVY);
    }

    private void BlockEffect(AbstractCard card, AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.GainBlock(block);
    }
}