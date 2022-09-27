package eatyourbeets.cards.animator.series.Konosuba;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import eatyourbeets.cards.animator.tokens.AffinityToken;
import eatyourbeets.cards.base.Affinity;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.effects.AttackEffects;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.attributes.AbstractAttribute;
import eatyourbeets.ui.cards.TargetEffectPreview;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class Mitsurugi extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Mitsurugi.class)
            .SetAttack(0, CardRarity.COMMON)
            .SetSeriesFromClassPackage()
            .PostInitialize(data ->
            {
                data.AddPreview(AffinityToken.GetCard(Affinity.Red), false);
                data.AddPreview(AffinityToken.GetCard(Affinity.Light), false);
            });

    private final TargetEffectPreview targetEffectPreview = new TargetEffectPreview(this::OnTargetChanged);
    private boolean showDamage = true;

    public Mitsurugi()
    {
        super(DATA);

        Initialize(10, 0);
        SetUpgrade(2, 0);

        SetAffinity_Red(1, 0, 1);
        SetAffinity_Light(1);
    }

    @Override
    public AbstractAttribute GetDamageInfo()
    {
        return showDamage ? super.GetDamageInfo() : null;
    }

    @Override
    public void update()
    {
        super.update();

        targetEffectPreview.Update();
    }

    @Override
    public void triggerOnExhaust()
    {
        super.triggerOnExhaust();

        final CardGroup group = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
        group.group.add(AffinityToken.GetCopy(Affinity.Red, false));
        group.group.add(AffinityToken.GetCopy(Affinity.Light, false));
        GameActions.Bottom.SelectFromPile(name, 1, group)
        .SetOptions(false, false)
        .AddCallback(cards2 ->
        {
            for (AbstractCard c : cards2)
            {
                GameActions.Bottom.MakeCardInHand(c);
            }
        });
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.GainForce(1, upgraded);

        if (GameUtilities.IsAttacking(m.intent))
        {
            GameActions.Bottom.DealDamage(this, m, AttackEffects.SLASH_HEAVY);
        }
    }

    private void OnTargetChanged(AbstractMonster monster)
    {
        showDamage = (monster == null || !GameUtilities.IsAttacking(monster.intent));
    }
}