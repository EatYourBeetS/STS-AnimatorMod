package eatyourbeets.cards.animator.beta.series.DateALive;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.animator.beta.special.Zadkiel;
import eatyourbeets.cards.base.*;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.TargetHelper;

public class YoshinoHimekawa extends AnimatorCard {
    public static final EYBCardData DATA = Register(YoshinoHimekawa.class).SetSkill(2, CardRarity.UNCOMMON, EYBCardTarget.ALL).SetSeriesFromClassPackage()
            .PostInitialize(data -> {
                data.AddPreview(new YoshinoHimekawa(true), false);
                data.AddPreview(new Zadkiel(), false);
            });
    private boolean transformed = false;

    private YoshinoHimekawa(boolean transformed) {
        this();

        SetTransformed(transformed);
    }

    public YoshinoHimekawa() {
        super(DATA);

        Initialize(0, 2, 5, 2);
        SetAffinity_Green(2, 0, 0);
        SetAffinity_Blue(1, 0, 0);
        SetAffinity_Star(0,0,1);

        SetEthereal(true);
        SetExhaust(true);
        SetHaste(true);
        SetCostUpgrade(-1);
    }

    @Override
    public void renderUpgradePreview(SpriteBatch sb) {
        if (!transformed) {
            super.renderUpgradePreview(sb);
        }
    }

    @Override
    public EYBCardPreview GetCardPreview() {
        if (transformed) {
            return null;
        }

        return super.GetCardPreview();
    }

    @Override
    public void triggerOnExhaust() {
        if (!transformed && CombatStats.TryActivateLimited(cardID)) {
            GameActions.Bottom.GainBlur(secondaryValue);
            GameActions.Top.MakeCardInDiscardPile(new Zadkiel()).SetUpgrade(upgraded, false);
        }
    }

    @Override
    public void triggerWhenDrawn() {
        if (!transformed) {
            GameActions.Top.Discard(this, player.hand).ShowEffect(true, true)
                    .AddCallback(() -> {
                        SetTransformed(true);
                        GameActions.Bottom.GainBlur(1);
                    })
                    .SetDuration(0.15f, true);
        }
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info) {
        GameActions.Bottom.GainBlock(block);
        if (transformed) {
            GameActions.Bottom.GainBlur(secondaryValue);
            GameActions.Bottom.ApplyFreezing(TargetHelper.Enemies(), magicNumber).ShowEffect(true, true);
        }
    }

    private void SetTransformed(boolean value) {
        transformed = value;

        if (transformed) {
            LoadImage("2");
            cardText.OverrideDescription(cardData.Strings.EXTENDED_DESCRIPTION[0], true);
        } else {
            LoadImage(null);
            cardText.OverrideDescription(null, true);
        }
    }
}